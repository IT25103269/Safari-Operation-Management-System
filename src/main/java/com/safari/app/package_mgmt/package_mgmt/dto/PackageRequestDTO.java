package com.safari.app.package_mgmt.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class PackageRequestDTO {

    @NotBlank(message = "Package title is required")
    @Size(min = 3, max = 150, message = "Package title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "National park location is required")
    @Size(min = 2, max = 150, message = "National park must be between 2 and 150 characters")
    private String nationalPark;

    @NotNull(message = "Duration in days is required")
    @Min(value = 1, message = "Duration must be at least 1 day")
    @jakarta.validation.constraints.Max(value = 30, message = "Duration cannot exceed 30 days")
    private Integer durationDays;

    @NotNull(message = "Price per person is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal pricePerPerson;

    @NotNull(message = "Max group size is required")
    @Min(value = 1, message = "Max group size must be at least 1")
    @jakarta.validation.constraints.Max(value = 100, message = "Max group size cannot exceed 100")
    private Integer maxGroupSize;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(max = 2000, message = "Included services cannot exceed 2000 characters")
    private String includedServices;

    @Size(max = 300, message = "Image URL cannot exceed 300 characters")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;}

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Size(max = 50, message = "A package cannot contain more than 50 itinerary items")
    @Valid
    private List<ItineraryDTO> itineraries;

    public static class ItineraryDTO {
        @jakarta.validation.constraints.Min(value = 1, message = "Itinerary day must be at least 1")
        private Integer dayNumber;

        @Size(max = 100, message = "Itinerary time slot cannot exceed 100 characters")
        private String timeSlot;

        @Size(max = 150, message = "Activity title cannot exceed 150 characters")
        private String activityTitle;

        @Size(max = 1000, message = "Itinerary description cannot exceed 1000 characters")
        private String description;

        public Integer getDayNumber() { return dayNumber; }
        public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
        public String getTimeSlot() { return timeSlot; }
        public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
        public String getActivityTitle() { return activityTitle; }
        public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNationalPark() { return nationalPark; }
    public void setNationalPark(String nationalPark) { this.nationalPark = nationalPark; }

    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }

    public BigDecimal getPricePerPerson() { return pricePerPerson; }
    public void setPricePerPerson(BigDecimal pricePerPerson) { this.pricePerPerson = pricePerPerson; }

    public Integer getMaxGroupSize() { return maxGroupSize; }
    public void setMaxGroupSize(Integer maxGroupSize) { this.maxGroupSize = maxGroupSize; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIncludedServices() { return includedServices; }
    public void setIncludedServices(String includedServices) { this.includedServices = includedServices; }

    public List<ItineraryDTO> getItineraries() { return itineraries; }
    public void setItineraries(List<ItineraryDTO> itineraries) { this.itineraries = itineraries; }
}