package com.safari.app.vehicle_mgmt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class VehicleRequestDTO {

    @NotBlank(message = "Registration number is required")
    @Size(min = 2, max = 50, message = "Registration number must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 .-]*$", message = "Registration number contains invalid characters")
    private String registrationNumber;

    @NotBlank(message = "Vehicle type is required")
    @Size(min = 2, max = 100, message = "Vehicle type must be between 2 and 100 characters")
    private String vehicleType;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1 passenger")
    private Integer capacity;

    @Size(max = 100, message = "Vehicle model cannot exceed 100 characters")
    private String model;

    @Pattern(regexp = "^$|DIESEL|PETROL|HYBRID|ELECTRIC", message = "Invalid fuel type")
    private String fuelType;

    @Size(max = 500, message = "Vehicle notes cannot exceed 500 characters")
    private String notes;

    @Size(max = 100, message = "Assigned driver name cannot exceed 100 characters")
    private String assignedDriverName;

    public VehicleRequestDTO() {}

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getAssignedDriverName() { return assignedDriverName; }
    public void setAssignedDriverName(String assignedDriverName) { this.assignedDriverName = assignedDriverName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}