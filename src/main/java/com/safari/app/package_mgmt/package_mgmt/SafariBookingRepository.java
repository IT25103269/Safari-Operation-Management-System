package com.safari.app.package_mgmt;

import com.safari.app.model.SafariBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SafariBookingRepository extends JpaRepository<SafariBooking, Long> {
    Optional<SafariBooking> findByBookingCode(String bookingCode);
    List<SafariBooking> findAllByOrderByCreatedAtDesc();
    List<SafariBooking> findByGuestEmailOrderByCreatedAtDesc(String guestEmail);
    List<SafariBooking> findByStatusOrderByCreatedAtDesc(String status);
    List<SafariBooking> findBySafariPackageIdOrderByCreatedAtDesc(Long packageId);
}
