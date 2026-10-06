package com.safari.app.sighting_mgmt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class SightingRequestDTO {

    @NotNull(message = "Species ID is required")
    private Long speciesId;

    @NotBlank(message = "Location name is required")
    @Size(min = 2, max = 150, message = "Location name must be between 2 and 150 characters")
    private String locationName;

    @NotNull(message = "Sighting date is required")
    @PastOrPresent(message = "Sighting date cannot be in the future")
    private LocalDate sightingDate;
    @Pattern(regexp = "^$|^(?:[01]\\d|2[0-3]):[0-5]\\d(?:\\s?[AP]M)?$", message = "Time must use HH:mm or HH:mm AM/PM format")
    private String timeOfDay;

    @Min(value = 1, message = "Count observed must be at least 1")
    private Integer countObserved = 1;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    @NotBlank(message = "Guide name is required")
    @Size(max = 100, message = "Guide name cannot exceed 100 characters")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "Guide name contains invalid characters")
    private String reportedByGuide;

    @Size(max = 150, message = "GPS coordinates cannot exceed 150 characters")
    private String gpsCoordinates;

    @Size(max = 1000, message = "Photo URL cannot exceed 1000 characters")
    @Pattern(regexp = "^$|^(https?://|/media/|/uploads/).+$", message = "Photo URL must be an HTTP(S), /media/ or /uploads/ URL")
    private String photoUrl;

    public SightingRequestDTO() {}

    public Long getSpeciesId() { return speciesId; }
    public void setSpeciesId(Long speciesId) { this.speciesId = speciesId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public LocalDate getSightingDate() { return sightingDate; }
    public void setSightingDate(LocalDate sightingDate) { this.sightingDate = sightingDate; }

    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }

    public Integer getCountObserved() { return countObserved; }
    public void setCountObserved(Integer countObserved) { this.countObserved = countObserved; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getReportedByGuide() { return reportedByGuide; }
    public void setReportedByGuide(String reportedByGuide) { this.reportedByGuide = reportedByGuide; }

    public String getGpsCoordinates() { return gpsCoordinates; }
    public void setGpsCoordinates(String gpsCoordinates) { this.gpsCoordinates = gpsCoordinates; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}