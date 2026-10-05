package com.safari.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Cottage number is required")
    @Size(max = 50, message = "Cottage number must not exceed 50 characters")
    @Column(name = "cottage_number", nullable = false, unique = true, length = 50)
    private String cottageNumber;

    @NotBlank(message = "Cottage name is required")
    @Size(max = 100, message = "Cottage name must not exceed 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9 &.\'()-]+$", message = "Cottage name contains invalid characters")
    @Column(name = "cottage_name", nullable = false, length = 100)
    private String cottageName;

    @NotBlank(message = "Cottage type is required")
    @Pattern(regexp = "^(STANDARD|DELUXE|LUXURY_SUITE|FAMILY_CHALET)$", message = "Invalid cottage type")
    @Column(name = "cottage_type", nullable = false, length = 50)
    private String cottageType; // STANDARD, DELUXE, LUXURY_SUITE, FAMILY_CHALET

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.01", message = "Base price must be greater than zero")
    @Column(name = "base_price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerNight;

    @NotNull(message = "Maximum occupancy is required")
    @Min(value = 1, message = "Maximum occupancy must be at least 1")
    @Max(value = 20, message = "Maximum occupancy cannot exceed 20")
    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 500, message = "Amenities must not exceed 500 characters")
    @Column(name = "amenities", length = 500)
    private String amenities; // e.g., "WiFi, AC, Plunge Pool, Forest View Deck"

    @Size(max = 1000, message = "Image URL must not exceed 1000 characters")
    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Size(max = 5000, message = "Gallery image URLs must not exceed 5000 characters")
    @Column(name = "gallery_image_urls", length = 5000)
    private String galleryImageUrls; // comma-separated additional image URLs

    @Pattern(regexp = "^(AVAILABLE|OCCUPIED|MAINTENANCE)$", message = "Invalid cottage status")
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGalleryImageUrls() {
        return galleryImageUrls;
    }

    public void setGalleryImageUrls(String galleryImageUrls) {
        this.galleryImageUrls = galleryImageUrls;
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