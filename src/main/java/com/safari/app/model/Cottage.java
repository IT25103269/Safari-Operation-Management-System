package com.safari.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "Cottages")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cottage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CottageID")
    private Integer id;

    @Column(name = "CottageNumber", length = 30)
    private String cottageNumber;

    @Column(name = "CottageName", nullable = false, length = 100)
    private String cottageName;

    @Column(name = "Park", nullable = false, length = 100)
    private String park = "Yala Buffer Zone";

    @Column(name = "RoomType", nullable = false, length = 50)
    private String roomType = "LUXURY_SUITE";

    @Column(name = "Capacity", nullable = false)
    private Integer capacity = 2;

    @Column(name = "PricePerNight", nullable = false)
    private Double pricePerNight = 150.00;

    @Column(name = "Status", nullable = false, length = 30)
    private String status = "AVAILABLE"; // AVAILABLE, OCCUPIED, MAINTENANCE, BOOKED

    @Column(name = "Amenities", length = 500)
    private String amenities;

    @Column(name = "ImageURL", length = 500)
    private String imageUrl;

    @Column(name = "Description", columnDefinition = "VARCHAR(MAX)")
    private String description;

    public Cottage() {}

    public Cottage(String cottageNumber, String cottageName, String park, String roomType, Integer capacity, Double pricePerNight, String amenities, String description) {
        this.cottageNumber = cottageNumber;
        this.cottageName = cottageName;
        this.park = park;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
        this.description = description;
        this.status = "AVAILABLE";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    // Alias for frontend compatibility
    public String getCottageType() {
        return roomType;
    }

    public void setCottageType(String cottageType) {
        this.roomType = cottageType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    // Alias for frontend compatibility
    public Integer getMaxOccupancy() {
        return capacity;
    }

    public void setMaxOccupancy(Integer maxOccupancy) {
        this.capacity = maxOccupancy;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    // Alias for frontend compatibility
    public Double getBasePricePerNight() {
        return pricePerNight;
    }

    public void setBasePricePerNight(Double basePricePerNight) {
        this.pricePerNight = basePricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
