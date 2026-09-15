package com.safari.app.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a Safari Cottage accommodation at Lanka Wild Trails.
 * Subsystem: Cottage Reservation Management (IT25101495)
 */
@Entity
@Table(name = "cottages")
public class Cottage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cottage_id")
    private Long id;

    @Column(name = "cottage_number", nullable = false, unique = true, length = 50)
    private String cottageNumber;

    @Column(name = "cottage_name", nullable = false, length = 100)
    private String cottageName;

    @Column(name = "cottage_type", nullable = false, length = 50)
    private String cottageType; // STANDARD, DELUXE, LUXURY_SUITE, FAMILY_CHALET

    @Column(name = "base_price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerNight;

    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "amenities", length = 500)
    private String amenities; // e.g., "WiFi, AC, Plunge Pool, Forest View Deck"

    @Column(name = "status", nullable = false, length = 30)
    private String status = "AVAILABLE"; // AVAILABLE, OCCUPIED, MAINTENANCE

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Cottage() {
    }

    public Cottage(Long id, String cottageNumber, String cottageName, String cottageType,
                   BigDecimal basePricePerNight, Integer maxOccupancy, String description,
                   String amenities, String status) {
        this.id = id;
        this.cottageNumber = cottageNumber;
        this.cottageName = cottageName;
        this.cottageType = cottageType;
        this.basePricePerNight = basePricePerNight;
        this.maxOccupancy = maxOccupancy;
        this.description = description;
        this.amenities = amenities;
        this.status = status != null ? status : "AVAILABLE";
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "AVAILABLE";
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

    public String getCottageNumber() {
        return cottageNumber;
    }

    public void setCottageNumber(String cottageNumber) {
        this.cottageNumber = cottageNumber;
    }

    public String getCottageName() {
        return cottageName;
    }

    public void setCottageName(String cottageName) {
        this.cottageName = cottageName;
    }

    public String getCottageType() {
        return cottageType;
    }

    public void setCottageType(String cottageType) {
        this.cottageType = cottageType;
    }

    public BigDecimal getBasePricePerNight() {
        return basePricePerNight;
    }

    public void setBasePricePerNight(BigDecimal basePricePerNight) {
        this.basePricePerNight = basePricePerNight;
    }

    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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