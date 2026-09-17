package com.safari.app.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "WildlifeSightings")
public class WildlifeSighting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SightingID")
    private Integer sightingId;

    @Column(name = "TripRef", length = 50)
    private String tripRef;

    @Column(name = "SpeciesName", nullable = false, length = 150)
    private String speciesName;

    @Column(name = "SightingDate", nullable = false)
    private LocalDate sightingDate;

    @Column(name = "SightingTime", nullable = false, length = 30)
    private String sightingTime;

    @Column(name = "ParkLocation", nullable = false, length = 100)
    private String parkLocation;

    @Column(name = "SpecificLocation", nullable = false, length = 255)
    private String specificLocation;

    @Column(name = "PhotoURL", length = 500)
    private String photoUrl;

    @Column(name = "Notes", length = 1000)
    private String notes;

    @Column(name = "LoggedBy", nullable = false, length = 100)
    private String loggedBy;

    @Column(name = "Status", nullable = false, length = 30)
    private String status = "Pending"; // Pending, Verified, Rejected

    @Column(name = "CreatedAt")
    private Timestamp createdAt;

    public WildlifeSighting() {
        this.status = "Pending";
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Timestamp(System.currentTimeMillis());
        }
        if (this.status == null) {
            this.status = "Pending";
        }
    }

    public Integer getSightingId() { return sightingId; }
    public void setSightingId(Integer sightingId) { this.sightingId = sightingId; }

    public String getTripRef() { return tripRef; }
    public void setTripRef(String tripRef) { this.tripRef = tripRef; }

    public String getSpeciesName() { return speciesName; }
    public void setSpeciesName(String speciesName) { this.speciesName = speciesName; }

    public LocalDate getSightingDate() { return sightingDate; }
    public void setSightingDate(LocalDate sightingDate) { this.sightingDate = sightingDate; }

    public String getSightingTime() { return sightingTime; }
    public void setSightingTime(String sightingTime) { this.sightingTime = sightingTime; }

    public String getParkLocation() { return parkLocation; }
    public void setParkLocation(String parkLocation) { this.parkLocation = parkLocation; }

    public String getSpecificLocation() { return specificLocation; }
    public void setSpecificLocation(String specificLocation) { this.specificLocation = specificLocation; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getLoggedBy() { return loggedBy; }
    public void setLoggedBy(String loggedBy) { this.loggedBy = loggedBy; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
