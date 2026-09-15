package com.safari.app.cottage_mgmt;

import com.safari.app.model.GuestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for GuestLog entity.
 * Subsystem: Cottage Reservation Management (IT25101495)
 */
@Repository
public interface GuestLogRepository extends JpaRepository<GuestLog, Long> {

    List<GuestLog> findByReservationId(Long reservationId);

    Optional<GuestLog> findTopByReservationIdOrderByActualCheckInTimeDesc(Long reservationId);

    List<GuestLog> findByLogStatus(String logStatus);

    List<GuestLog> findAllByOrderByCreatedAtDesc();
}