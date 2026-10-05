package com.safari.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity tracking penalties assessed for overstays, damages, and violations.
 * Subsystem: Cottage Reservation Management (IT25101495)
 */
@Entity
@Table(name = "fine_records")
public class FineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fine_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", nullable = false)
    @JsonIgnoreProperties({"guestLogs", "fineRecords"})
    private CottageReservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_log_id")
    @JsonIgnoreProperties({"reservation"})
    private GuestLog guestLog;

    @Column(name = "fine_type", nullable = false, length = 50)
    private String fineType; // OVERSTAY, ROOM_DAMAGE, LATE_CHECKOUT, OTHER

    @Column(name = "fine_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal fineAmount;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus = "PENDING"; // PENDING, PAID, WAIVED

    @Column(name = "assessed_by", length = 100)
    private String assessedBy;

    @Column(name = "assessed_at")
    private LocalDateTime assessedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public FineRecord() {
    }

    public FineRecord(CottageReservation reservation, GuestLog guestLog, String fineType,
                      BigDecimal fineAmount, String reason, String assessedBy) {
        this.reservation = reservation;
        this.guestLog = guestLog;
        this.fineType = fineType;
        this.fineAmount = fineAmount;
        this.reason = reason;
        this.assessedBy = assessedBy;
        this.paymentStatus = "PENDING";
        this.assessedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.assessedAt == null) {
            this.assessedAt = LocalDateTime.now();
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = "PENDING";
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CottageReservation getReservation() {
        return reservation;
    }

    public void setReservation(CottageReservation reservation) {
        this.reservation = reservation;
    }

    public GuestLog getGuestLog() {
        return guestLog;
    }

    public void setGuestLog(GuestLog guestLog) {
        this.guestLog = guestLog;
    }

    public String getFineType() {
        return fineType;
    }

    public void setFineType(String fineType) {
        this.fineType = fineType;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAssessedBy() {
        return assessedBy;
    }

    public void setAssessedBy(String assessedBy) {
        this.assessedBy = assessedBy;
    }

    public LocalDateTime getAssessedAt() {
        return assessedAt;
    }

    public void setAssessedAt(LocalDateTime assessedAt) {
        this.assessedAt = assessedAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}