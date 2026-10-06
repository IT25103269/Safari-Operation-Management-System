package com.safari.app.package_mgmt;

import com.safari.app.model.SafariBooking;
import com.safari.app.model.SafariPackage;
import com.safari.app.package_mgmt.dto.SafariBookingRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SafariBookingService {

    private final SafariBookingRepository bookingRepository;
    private final SafariPackageRepository packageRepository;
    private final BookingPricingStrategy pricingStrategy;

    @Autowired
    public SafariBookingService(SafariBookingRepository bookingRepository,
                                SafariPackageRepository packageRepository,
                                BookingPricingStrategy pricingStrategy) {
        this.bookingRepository = bookingRepository;
        this.packageRepository = packageRepository;
        this.pricingStrategy = pricingStrategy;
    }

    public SafariBooking createBooking(SafariBookingRequestDTO req) {
        SafariPackage pkg = packageRepository.findById(req.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Safari package not found with ID: " + req.getPackageId()));

        if (!"ACTIVE".equalsIgnoreCase(pkg.getStatus())) {
            throw new IllegalStateException("This safari package is not currently available for booking.");
        }

        if (req.getSafariDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Safari date must be today or in the future.");
        }

        if (pkg.getMaxGroupSize() == null || pkg.getMaxGroupSize() < 1) {
            throw new IllegalStateException("This safari package has an invalid maximum group size and cannot be booked.");
        }

        if (req.getNumberOfTravellers() > pkg.getMaxGroupSize()) {
            throw new IllegalArgumentException("This package allows a maximum of " + pkg.getMaxGroupSize() + " travellers.");
        }

        String slot = req.getTimeSlot().trim().toUpperCase();
        if (!List.of("MORNING", "EVENING", "FULL_DAY").contains(slot)) {
            throw new IllegalArgumentException("Time slot must be MORNING, EVENING or FULL_DAY.");
        }

        SafariBooking booking = new SafariBooking();
        booking.setBookingCode("SAF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setSafariPackage(pkg);
        booking.setUserId(req.getUserId());
        booking.setGuestName(req.getGuestName().trim());
        booking.setGuestEmail(req.getGuestEmail().trim().toLowerCase());
        booking.setGuestPhone(req.getGuestPhone().trim());
        booking.setSafariDate(req.getSafariDate());
        booking.setNumberOfTravellers(req.getNumberOfTravellers());
        booking.setTimeSlot(slot);
        booking.setSpecialRequests(req.getSpecialRequests());
        booking.setTotalAmount(pricingStrategy.calculateTotal(pkg, req.getNumberOfTravellers()));
        booking.setStatus("PENDING");

        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<SafariBooking> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public SafariBooking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Safari booking not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<SafariBooking> getBookingsByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Guest email is required.");
        }
        return bookingRepository.findByGuestEmailOrderByCreatedAtDesc(email.trim().toLowerCase());
    }

    @Transactional(readOnly = true)
    public List<SafariBooking> getBookingsByStatus(String status) {
        String normalized = status == null ? "" : status.trim().toUpperCase();
        if (!List.of("PENDING", "CONFIRMED", "CANCELLED", "COMPLETED").contains(normalized)) {
            throw new IllegalArgumentException("Invalid safari booking status: " + status);
        }
        return bookingRepository.findByStatusOrderByCreatedAtDesc(normalized);
    }

    @Transactional(readOnly = true)
    public SafariBooking getBookingByCode(String code) {
        return bookingRepository.findByBookingCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Safari booking not found with reference: " + code));
    }

    public SafariBooking updateStatus(Long id, String status) {
        SafariBooking booking = getBookingById(id);
        String normalized = status == null ? "" : status.trim().toUpperCase();
        if (!List.of("PENDING", "CONFIRMED", "CANCELLED", "COMPLETED").contains(normalized)) {
            throw new IllegalArgumentException("Invalid safari booking status: " + status);
        }
        if ("COMPLETED".equals(normalized) && !"CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalStateException("Only CONFIRMED safari bookings can be completed.");
        }
        if ("CONFIRMED".equals(normalized) && "CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalStateException("A cancelled safari booking cannot be confirmed again.");
        }
        booking.setStatus(normalized);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.delete(getBookingById(id));
    }
}
