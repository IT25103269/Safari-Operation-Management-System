package com.safari.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true, length = 30)
    private String registrationNumber;

    @Column(name = "vehicle_type", nullable = false, length = 50)
    private String vehicleType;

    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity;

    @Column(name = "assigned_driver", length = 100)
    private String assignedDriver;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "AVAILABLE"; // AVAILABLE, IN_USE, UNDER_MAINTENANCE, UNAVAILABLE

    @Column(name = "description", length = 500)
    private String description;

    public Vehicle() {}

    public Vehicle(String registrationNumber, String vehicleType, Integer seatingCapacity,
                   String assignedDriver, String status, String description) {
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.seatingCapacity = seatingCapacity;
        this.assignedDriver = assignedDriver;
        this.status = status != null ? status : "AVAILABLE";
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public String getAssignedDriver() { return assignedDriver; }
    public void setAssignedDriver(String assignedDriver) { this.assignedDriver = assignedDriver; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
