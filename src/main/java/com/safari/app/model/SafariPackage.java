package com.safari.app.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SafariPackages")
public class SafariPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PackageID")
    private Integer packageId;

    @Column(name = "PackageName", nullable = false, length = 150)
    private String packageName;

    @Column(name = "Destination", nullable = false, length = 100)
    private String destination;

    @Column(name = "DurationDays", nullable = false)
    private Integer durationDays;

    @Column(name = "PricePerPerson", nullable = false)
    private Double pricePerPerson;

    @Column(name = "MaxGroupSize", nullable = false)
    private Integer maxGroupSize;

    @Column(name = "ItinerarySummary", columnDefinition = "VARCHAR(MAX)")
    private String itinerarySummary;

    @Column(name = "IncludedServices", columnDefinition = "VARCHAR(MAX)")
    private String includedServices;

    @Column(name = "Status", length = 20)
    private String status = "Active"; // Active, Archived

    @Column(name = "CreatedAt")
    private Timestamp createdAt;

    @Column(name = "UpdatedAt")
    private Timestamp updatedAt;

    public SafariPackage() {
        this.status = "Active";
    }

    public SafariPackage(String packageName, String destination, Integer durationDays,
                         Double pricePerPerson, Integer maxGroupSize, String itinerarySummary,
                         String includedServices) {
        this.packageName = packageName;
        this.destination = destination;
        this.durationDays = durationDays;
        this.pricePerPerson = pricePerPerson;
        this.maxGroupSize = maxGroupSize;
        this.itinerarySummary = itinerarySummary;
        this.includedServices = includedServices;
        this.status = "Active";
    }

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = "Active";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }

    public Double getPricePerPerson() { return pricePerPerson; }
    public void setPricePerPerson(Double pricePerPerson) { this.pricePerPerson = pricePerPerson; }

    public Integer getMaxGroupSize() { return maxGroupSize; }
    public void setMaxGroupSize(Integer maxGroupSize) { this.maxGroupSize = maxGroupSize; }

    public String getItinerarySummary() { return itinerarySummary; }
    public void setItinerarySummary(String itinerarySummary) { this.itinerarySummary = itinerarySummary; }

    public String getIncludedServices() { return includedServices; }
    public void setIncludedServices(String includedServices) { this.includedServices = includedServices; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
