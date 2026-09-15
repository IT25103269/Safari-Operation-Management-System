package com.safari.app.cottage_mgmt;

import com.safari.app.model.CottageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for CottageReservation entity.
 * Subsystem: Cottage Reservation Management (IT25101495)
 */
@Repository
public interface ReservationRepository extends JpaRepository<CottageReservation, Long> {

    Optional<CottageReservation> findByReservationCode(String reservationCode);

    List<CottageReservation> findByStatus(String status);

    List<CottageReservation> findByCottageId(Long cottageId);

    List<CottageReservation> findByGuestEmail(String guestEmail);

    List<CottageReservation> findAllByOrderByCreatedAtDesc();

    /**
     * Checks if a cottage has an overlapping active reservation for dates [checkIn, checkOut).
     */
    @Query("SELECT COUNT(r) FROM CottageReservation r WHERE r.cottage.id = :cottageId " +
           "AND (:excludeId IS NULL OR r.id <> :excludeId) " +
           "AND r.status NOT IN ('CANCELLED', 'REJECTED') " +
           "AND r.checkInDate < :checkOut AND r.checkOutDate > :checkIn")
    long countConflictingReservations(@Param("cottageId") Long cottageId,
                                     @Param("checkIn") LocalDate checkIn,
                                     @Param("checkOut") LocalDate checkOut,
                                     @Param("excludeId") Long excludeId);

    @Query("SELECT r FROM CottageReservation r WHERE " +
           "(r.checkInDate BETWEEN :start AND :end) OR (r.checkOutDate BETWEEN :start AND :end)")
    List<CottageReservation> findReservationsInDateRange(@Param("start") LocalDate start,
                                                         @Param("end") LocalDate end);

    long countByStatus(String status);
}