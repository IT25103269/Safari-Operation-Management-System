package com.safari.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity logging guest arrival, departure, overstay and inspection records.
 * Subsystem: Cottage Reservation Management (IT25101495)
 */
@Entity
@Table(name = "guest_logs")
public class GuestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", nullable = false)
    @JsonIgnoreProperties({"guestLogs", "fineRecords"})
    private CottageReservation reservation;

    @Column(name = "actual_check_in_time")
    private LocalDateTime actualCheckInTime;

    @Column(name = "actual_check_out_time")
    private LocalDateTime actualCheckOutTime;

    @Column(name = "scheduled_check_out_time", nullable = false)
    private LocalDateTime scheduledCheckOutTime;

    @Column(name = "key_card_number", length = 50)
    private String keyCardNumber;

    @Column(name = "overstay_minutes")
    private Integer overstayMinutes = 0;

    @Column(name = "overstay_fee", precision = 10, scale = 2)
    private BigDecimal overstayFee = BigDecimal.ZERO;

    @Column(name = "damage_assessed")
    private Boolean damageAssessed = false;

    @Column(name = "damage_description", length = 500)
    private String damageDescription;

    @Column(name = "damage_fee", precision = 10, scale = 2)
    private BigDecimal damageFee = BigDecimal.ZERO;

    @Column(name = "log_status", nullable = false, length = 30)
    private String logStatus = "ACTIVE"; // CHECKED_IN, CHECKED_OUT, COMPLETED

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public GuestLog() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.overstayMinutes == null) {
            this.overstayMinutes = 0;
        }
        if (this.overstayFee == null) {
            this.overstayFee = BigDecimal.ZERO;
        }
        if (this.damageAssessed == null) {
            this.damageAssessed = false;
        }
        if (this.damageFee == null) {
            this.damageFee = BigDecimal.ZERO;
        }
        if (this.logStatus == null) {
            this.logStatus = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public LocalDateTime getActualCheckInTime() {
        return actualCheckInTime;
    }

    public void setActualCheckInTime(LocalDateTime actualCheckInTime) {
        this.actualCheckInTime = actualCheckInTime;
    }

    public LocalDateTime getActualCheckOutTime() {
        return actualCheckOutTime;
    }

    public void setActualCheckOutTime(LocalDateTime actualCheckOutTime) {
        this.actualCheckOutTime = actualCheckOutTime;
    }

    public LocalDateTime getScheduledCheckOutTime() {
        return scheduledCheckOutTime;
    }

    public void setScheduledCheckOutTime(LocalDateTime scheduledCheckOutTime) {
        this.scheduledCheckOutTime = scheduledCheckOutTime;
    }

    public String getKeyCardNumber() {
        return keyCardNumber;
    }

    public void setKeyCardNumber(String keyCardNumber) {
        this.keyCardNumber = keyCardNumber;
    }

    public Integer getOverstayMinutes() {
        return overstayMinutes;
    }

    public void setOverstayMinutes(Integer overstayMinutes) {
        this.overstayMinutes = overstayMinutes;
    }

    public BigDecimal getOverstayFee() {
        return overstayFee;
    }

    public void setOverstayFee(BigDecimal overstayFee) {
        this.overstayFee = overstayFee;
    }

    public Boolean getDamageAssessed() {
        return damageAssessed;
    }

    public void setDamageAssessed(Boolean damageAssessed) {
        this.damageAssessed = damageAssessed;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public BigDecimal getDamageFee() {
        return damageFee;
    }

    public void setDamageFee(BigDecimal damageFee) {
        this.damageFee = damageFee;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}