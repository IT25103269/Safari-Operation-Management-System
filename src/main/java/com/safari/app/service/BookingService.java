package com.safari.app.service;

import com.safari.app.model.SafariBooking;
import com.safari.app.model.SafariPackage;
import com.safari.app.model.User;
import com.safari.app.model.Vehicle;
import com.safari.app.repository.BookingRepository;
import com.safari.app.repository.SafariPackageRepository;
import com.safari.app.repository.UserRepository;
import com.safari.app.repository.VehicleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SafariPackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public SafariBooking createBooking(SafariBooking booking, HttpSession session) {
        if (booking.getPackageId() == null) {
            throw new IllegalArgumentException("Package ID is required.");
        }

        SafariPackage pkg = packageRepository.findById(booking.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + booking.getPackageId()));

        if (booking.getTravelDate() == null || booking.getTravelDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Travel date cannot be in the past.");
        }

        if (booking.getParticipantsCount() == null || booking.getParticipantsCount() <= 0) {
            throw new IllegalArgumentException("Number of participants must be greater than zero.");
        }

        if (pkg.getMaxGroupSize() != null && booking.getParticipantsCount() > pkg.getMaxGroupSize()) {
            throw new IllegalArgumentException("Number of participants exceeds package maximum group size (" + pkg.getMaxGroupSize() + ").");
        }

        // Connect user session if available
        Integer loggedInId = session != null ? (Integer) session.getAttribute("LOGGED_IN_USER_ID") : null;
        if (loggedInId != null) {
            Optional<User> uOpt = userRepository.findById(loggedInId);
            if (uOpt.isPresent()) {
                User u = uOpt.get();
                booking.setTouristId(u.getUserId());
                if (booking.getTouristName() == null || booking.getTouristName().trim().isEmpty()) {
                    booking.setTouristName(u.getFullName());
                }
                if (booking.getTouristEmail() == null || booking.getTouristEmail().trim().isEmpty()) {
                    booking.setTouristEmail(u.getEmail());
                }
            }
        }

        if (booking.getTouristName() == null || booking.getTouristName().trim().isEmpty()) {
            booking.setTouristName("Guest Tourist");
        }
        if (booking.getTouristEmail() == null || booking.getTouristEmail().trim().isEmpty()) {
            booking.setTouristEmail("tourist@lankawildtrails.lk");
        }

        booking.setPackageName(pkg.getPackageName());
        booking.setDestination(pkg.getDestination());
        booking.setTotalPrice(pkg.getPricePerPerson() * booking.getParticipantsCount());

        // Generate unique reference
        String ref;
        do {
            ref = "LWT-" + LocalDate.now().getYear() + "-" + String.format("%04d", new Random().nextInt(10000));
        } while (bookingRepository.findByBookingRef(ref).isPresent());

        booking.setBookingRef(ref);
        booking.setStatus("PENDING");
        return bookingRepository.save(booking);
    }

    public List<SafariBooking> getMyBookings(HttpSession session) {
        Integer loggedInId = session != null ? (Integer) session.getAttribute("LOGGED_IN_USER_ID") : null;
        String loggedInEmail = session != null ? (String) session.getAttribute("LOGGED_IN_USER_EMAIL") : null;

        if (loggedInId != null) {
            List<SafariBooking> list = bookingRepository.findByTouristIdOrderByCreatedAtDesc(loggedInId);
            if (!list.isEmpty()) return list;
        }

        if (loggedInEmail != null) {
            List<SafariBooking> list = bookingRepository.findByTouristEmailIgnoreCaseOrderByCreatedAtDesc(loggedInEmail);
            if (!list.isEmpty()) return list;
        }

        // Default fallback to all bookings if not filtered
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<SafariBooking> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    public SafariBooking getBookingById(Integer id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
    }

    public SafariBooking updateStatus(Integer id, String status) {
        SafariBooking b = getBookingById(id);
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }
        String s = status.trim().toUpperCase();
        if (!List.of("PENDING", "CONFIRMED", "ALLOCATED", "COMPLETED", "CANCELLED").contains(s)) {
            throw new IllegalArgumentException("Invalid booking status: " + status + ". Allowed values: PENDING, CONFIRMED, ALLOCATED, COMPLETED, CANCELLED.");
        }
        b.setStatus(s);
        return bookingRepository.save(b);
    }

    public SafariBooking cancelBooking(Integer id) {
        SafariBooking b = getBookingById(id);
        b.setStatus("CANCELLED");

        // Release vehicle if one was assigned
        if (b.getAssignedVehicle() != null && !b.getAssignedVehicle().equalsIgnoreCase("Unassigned")) {
            List<Vehicle> allVehicles = vehicleRepository.findAll();
            for (Vehicle v : allVehicles) {
                if (b.getAssignedVehicle().contains(v.getRegistrationNumber())) {
                    v.setStatus("AVAILABLE");
                    vehicleRepository.save(v);
                    break;
                }
            }
        }

        return bookingRepository.save(b);
    }

    public SafariBooking allocateResources(Integer bookingId, String guide, String vehicleStr) {
        SafariBooking booking = getBookingById(bookingId);

        if (guide == null || guide.trim().isEmpty() || guide.equalsIgnoreCase("Unassigned")) {
            booking.setAssignedGuide("Unassigned");
        } else {
            booking.setAssignedGuide(guide.trim());
        }

        if (vehicleStr == null || vehicleStr.trim().isEmpty() || vehicleStr.equalsIgnoreCase("Unassigned")) {
            booking.setAssignedVehicle("Unassigned");
            booking.setStatus("PENDING");
            return bookingRepository.save(booking);
        }

        // Extract registration number or find vehicle
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        Vehicle selectedVehicle = null;
        for (Vehicle v : allVehicles) {
            if (vehicleStr.contains(v.getRegistrationNumber())) {
                selectedVehicle = v;
                break;
            }
        }

        if (selectedVehicle == null) {
            throw new IllegalArgumentException("Vehicle could not be found in fleet.");
        }

        // Validate vehicle capacity
        if (selectedVehicle.getSeatingCapacity() < booking.getParticipantsCount()) {
            throw new IllegalArgumentException(String.format(
                    "Vehicle capacity (%d passengers) is insufficient for booking headcount (%d passengers).",
                    selectedVehicle.getSeatingCapacity(), booking.getParticipantsCount()
            ));
        }

        // Validate vehicle status
        if ("UNDER_MAINTENANCE".equalsIgnoreCase(selectedVehicle.getStatus()) ||
            "UNAVAILABLE".equalsIgnoreCase(selectedVehicle.getStatus())) {
            throw new IllegalArgumentException("Selected vehicle is currently " + selectedVehicle.getStatus() + " and cannot be assigned.");
        }

        // Conflict check: Check if vehicle is already allocated to another trip on the same travel date
        List<SafariBooking> dateBookings = bookingRepository.findByTravelDateAndStatusNot(booking.getTravelDate(), "CANCELLED");
        for (SafariBooking other : dateBookings) {
            if (!other.getBookingId().equals(booking.getBookingId())) {
                if (other.getAssignedVehicle() != null && other.getAssignedVehicle().contains(selectedVehicle.getRegistrationNumber())) {
                    throw new IllegalArgumentException(String.format(
                            "Vehicle %s is already allocated on %s to Booking %s.",
                            selectedVehicle.getRegistrationNumber(), booking.getTravelDate(), other.getBookingRef()
                    ));
                }
            }
        }

        booking.setAssignedVehicle(selectedVehicle.getRegistrationNumber() + " (" + selectedVehicle.getVehicleType() + " - Cap: " + selectedVehicle.getSeatingCapacity() + ")");
        selectedVehicle.setStatus("IN_USE");
        vehicleRepository.save(selectedVehicle);

        booking.setStatus("ALLOCATED");
        return bookingRepository.save(booking);
    }
}
