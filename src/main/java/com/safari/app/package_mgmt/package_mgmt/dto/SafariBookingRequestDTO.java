package com.safari.app.package_mgmt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class SafariBookingRequestDTO {

    @NotNull(message = "Safari package ID is required")
    private Long packageId;

    private Long userId;

    @NotBlank(message = "Guest full name is required")
    @Size(min = 2, max = 100, message = "Guest name must be between 2 and 100 characters")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "Guest name contains invalid characters")
    private String guestName;

    @NotBlank(message = "Guest email is required")
    @Email(message = "Enter a valid email address")
    private String guestEmail;

    @NotBlank(message = "Guest phone number is required")
    @Pattern(regexp = "^\\+?[0-9][0-9 .()-]{6,19}$", message = "Enter a valid guest phone number")
    private String guestPhone;

    @NotNull(message = "Safari date is required")
    @FutureOrPresent(message = "Safari date must be today or in the future")
    private LocalDate safariDate;

    @NotNull(message = "Number of travellers is required")
    @Min(value = 1, message = "At least 1 traveller is required")
    @jakarta.validation.constraints.Max(value = 100, message = "Travellers cannot exceed 100")
    private Integer numberOfTravellers;

    @NotBlank(message = "Time slot is required")
    @Pattern(regexp = "MORNING|EVENING|FULL_DAY", message = "Invalid safari time slot")
    private String timeSlot;

    @Size(max = 1000, message = "Special requests cannot exceed 1000 characters")
    private String specialRequests;

    public SafariBookingRequestDTO() {}

    public Long getPackageId() { return packageId; }
    public void setPackageId(Long packageId) { this.packageId = packageId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public String getGuestEmail() { return guestEmail; }
    public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }
    public String getGuestPhone() { return guestPhone; }
    public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }
    public LocalDate getSafariDate() { return safariDate; }
    public void setSafariDate(LocalDate safariDate) { this.safariDate = safariDate; }
    public Integer getNumberOfTravellers() { return numberOfTravellers; }
    public void setNumberOfTravellers(Integer numberOfTravellers) { this.numberOfTravellers = numberOfTravellers; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}
