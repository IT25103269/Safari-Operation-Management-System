package com.safari.app.vehicle_mgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class VehicleStatusDTO {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "AVAILABLE|MAINTENANCE|UNAVAILABLE", message = "Invalid vehicle status")
    private String status; // AVAILABLE, MAINTENANCE, UNAVAILABLE

    @Size(max = 500, message = "Status notes cannot exceed 500 characters")
    private String notes;

    public VehicleStatusDTO() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}