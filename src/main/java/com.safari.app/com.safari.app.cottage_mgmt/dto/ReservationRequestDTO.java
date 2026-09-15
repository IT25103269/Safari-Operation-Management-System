package com.safari.app.cottage_mgmt.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservationRequestDTO {

    @NotNull(message = "Cottage ID is required")
    private Long cottageId;

    private Long userId;

    @NotBlank(message = "Guest full name is required")
    private String guestName;

    @NotBlank(message = "Guest email is required")
    private String guestEmail;

    @NotBlank(message = "Guest phone number is required")
    private String guestPhone;

    @NotNull(message = "Check-in date is required")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    private LocalDate checkOutDate;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "At least 1 guest is required")
    private Integer numberOfGuests;

    @Min(value = 0, message = "Extra beds cannot be negative")
    private Integer extraBeds = 0;

    private String specialRequests;

    public ReservationRequestDTO() {}

    public Long getCottageId() { return cottageId; }
    public void setCottageId(Long cottageId) { this.cottageId = cottageId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getGuestEmail() { return guestEmail; }
    public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }

    public String getGuestPhone() { return guestPhone; }
    public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public Integer getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(Integer numberOfGuests) { this.numberOfGuests = numberOfGuests; }

    public Integer getExtraBeds() { return extraBeds; }
    public void setExtraBeds(Integer extraBeds) { this.extraBeds = extraBeds; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}