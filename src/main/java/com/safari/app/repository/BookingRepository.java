package com.safari.app.repository;

import com.safari.app.model.SafariBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<SafariBooking, Integer> {
    Optional<SafariBooking> findByBookingRef(String bookingRef);
    List<SafariBooking> findByTouristEmailIgnoreCaseOrderByCreatedAtDesc(String email);
    List<SafariBooking> findByTouristIdOrderByCreatedAtDesc(Integer touristId);
    List<SafariBooking> findByAssignedVehicleContainingIgnoreCase(String vehicle);
    List<SafariBooking> findByAssignedGuideContainingIgnoreCase(String guide);
    List<SafariBooking> findByTravelDateAndStatusNot(LocalDate travelDate, String excludedStatus);
    List<SafariBooking> findAllByOrderByCreatedAtDesc();
}
