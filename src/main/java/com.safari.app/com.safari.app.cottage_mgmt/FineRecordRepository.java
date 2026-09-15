package com.safari.app.cottage_mgmt;

import com.safari.app.model.FineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA Repository for FineRecord entity.
 * Subsystem: Cottage Reservation Management (IT25101495)
 */
@Repository
public interface FineRecordRepository extends JpaRepository<FineRecord, Long> {

    List<FineRecord> findByReservationId(Long reservationId);

    List<FineRecord> findByPaymentStatus(String paymentStatus);

    List<FineRecord> findAllByOrderByAssessedAtDesc();

    @Query("SELECT COALESCE(SUM(f.fineAmount), 0) FROM FineRecord f WHERE f.paymentStatus = 'PAID'")
    BigDecimal sumTotalPaidFines();

    @Query("SELECT COALESCE(SUM(f.fineAmount), 0) FROM FineRecord f WHERE f.reservation.id = :reservationId")
    BigDecimal sumFinesByReservationId(@Param("reservationId") Long reservationId);
}