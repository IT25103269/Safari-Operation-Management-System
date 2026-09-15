package com.safari.app.cottage_mgmt;

import com.safari.app.model.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Cottage entity.
 * Subsystem: Cottage Reservation Management (IT25101495)
 */
@Repository
public interface CottageRepository extends JpaRepository<Cottage, Long> {

    Optional<Cottage> findByCottageNumber(String cottageNumber);

    List<Cottage> findByStatus(String status);

    List<Cottage> findByCottageType(String cottageType);

    /**
     * Real-time query finding cottages with no conflicting active reservations in [checkIn, checkOut).
     */
    @Query("SELECT c FROM Cottage c WHERE c.status = 'AVAILABLE' " +
           "AND c.id NOT IN (" +
           "    SELECT r.cottage.id FROM CottageReservation r " +
           "    WHERE r.status NOT IN ('CANCELLED', 'REJECTED') " +
           "    AND r.checkInDate < :checkOut AND r.checkOutDate > :checkIn" +
           ")")
    List<Cottage> findAvailableCottages(@Param("checkIn") LocalDate checkIn,
                                       @Param("checkOut") LocalDate checkOut);

    @Query("SELECT c FROM Cottage c WHERE c.status = 'AVAILABLE' " +
           "AND c.maxOccupancy >= :guests " +
           "AND (:type IS NULL OR c.cottageType = :type) " +
           "AND c.id NOT IN (" +
           "    SELECT r.cottage.id FROM CottageReservation r " +
           "    WHERE r.status NOT IN ('CANCELLED', 'REJECTED') " +
           "    AND r.checkInDate < :checkOut AND r.checkOutDate > :checkIn" +
           ")")
    List<Cottage> findAvailableCottagesWithFilter(@Param("checkIn") LocalDate checkIn,
                                                 @Param("checkOut") LocalDate checkOut,
                                                 @Param("guests") Integer guests,
                                                 @Param("type") String type);
}