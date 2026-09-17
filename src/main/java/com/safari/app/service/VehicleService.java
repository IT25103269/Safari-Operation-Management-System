package com.safari.app.service;

import com.safari.app.model.Vehicle;
import com.safari.app.model.VehicleMaintenanceIssue;
import com.safari.app.repository.VehicleMaintenanceRepository;
import com.safari.app.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private static final List<String> VALID_STATUSES = Arrays.asList(
            "AVAILABLE", "IN_USE", "UNDER_MAINTENANCE", "UNAVAILABLE"
    );

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMaintenanceRepository maintenanceRepository;

    public List<Vehicle> getAllVehicles(String search, String status) {
        List<Vehicle> all = vehicleRepository.findAll();

        return all.stream()
                .filter(v -> {
                    boolean matchSearch = (search == null || search.trim().isEmpty())
                            || (v.getRegistrationNumber() != null && v.getRegistrationNumber().toLowerCase().contains(search.toLowerCase().trim()))
                            || (v.getVehicleType() != null && v.getVehicleType().toLowerCase().contains(search.toLowerCase().trim()))
                            || (v.getAssignedDriver() != null && v.getAssignedDriver().toLowerCase().contains(search.toLowerCase().trim()));

                    boolean matchStatus = (status == null || status.trim().isEmpty() || status.equalsIgnoreCase("ALL"))
                            || (v.getStatus() != null && v.getStatus().equalsIgnoreCase(status.trim()));

                    return matchSearch && matchStatus;
                })
                .collect(Collectors.toList());
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + id));
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        validateVehicle(vehicle, null);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle updated) {
        Vehicle existing = getVehicleById(id);
        validateVehicle(updated, id);

        existing.setRegistrationNumber(updated.getRegistrationNumber().trim());
        existing.setVehicleType(updated.getVehicleType().trim());
        existing.setSeatingCapacity(updated.getSeatingCapacity());
        existing.setAssignedDriver(updated.getAssignedDriver() != null ? updated.getAssignedDriver().trim() : null);
        existing.setStatus(updated.getStatus().trim().toUpperCase());
        existing.setDescription(updated.getDescription());

        return vehicleRepository.save(existing);
    }

    public Vehicle updateStatus(Long id, String status) {
        Vehicle existing = getVehicleById(id);
        if (status == null || !VALID_STATUSES.contains(status.trim().toUpperCase())) {
            throw new IllegalArgumentException("Invalid vehicle status: " + status);
        }
        existing.setStatus(status.trim().toUpperCase());
        return vehicleRepository.save(existing);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        // Also delete associated maintenance records if any
        List<VehicleMaintenanceIssue> issues = maintenanceRepository.findByVehicleIdOrderByReportedAtDesc(id);
        if (!issues.isEmpty()) {
            maintenanceRepository.deleteAll(issues);
        }
        vehicleRepository.delete(vehicle);
    }

    public List<VehicleMaintenanceIssue> getMaintenanceIssues(Long vehicleId) {
        return maintenanceRepository.findByVehicleIdOrderByReportedAtDesc(vehicleId);
    }

    public VehicleMaintenanceIssue reportMaintenanceIssue(Long vehicleId, VehicleMaintenanceIssue issue) {
        Vehicle vehicle = getVehicleById(vehicleId);
        if (issue.getIssueTitle() == null || issue.getIssueTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Issue title cannot be empty.");
        }
        if (issue.getDescription() == null || issue.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Issue description cannot be empty.");
        }

        issue.setVehicleId(vehicle.getId());
        if (issue.getStatus() == null || issue.getStatus().trim().isEmpty()) {
            issue.setStatus("OPEN");
        }
        VehicleMaintenanceIssue saved = maintenanceRepository.save(issue);

        // If priority is high, mark vehicle as UNDER_MAINTENANCE automatically
        if ("High".equalsIgnoreCase(issue.getPriority())) {
            vehicle.setStatus("UNDER_MAINTENANCE");
            vehicleRepository.save(vehicle);
        }

        return saved;
    }

    public List<Vehicle> getAvailableVehicles(Integer minCapacity) {
        int cap = (minCapacity != null && minCapacity > 0) ? minCapacity : 1;
        return vehicleRepository.findByStatusIgnoreCaseAndSeatingCapacityGreaterThanEqual("AVAILABLE", cap);
    }

    private void validateVehicle(Vehicle vehicle, Long currentId) {
        if (vehicle.getRegistrationNumber() == null || vehicle.getRegistrationNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number is required.");
        }

        String regNo = vehicle.getRegistrationNumber().trim();
        if (currentId == null) {
            if (vehicleRepository.existsByRegistrationNumberIgnoreCase(regNo)) {
                throw new IllegalArgumentException("Registration number '" + regNo + "' is already registered.");
            }
        } else {
            if (vehicleRepository.existsByRegistrationNumberIgnoreCaseAndIdNot(regNo, currentId)) {
                throw new IllegalArgumentException("Registration number '" + regNo + "' is already registered to another vehicle.");
            }
        }

        if (vehicle.getVehicleType() == null || vehicle.getVehicleType().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle type is required.");
        }

        if (vehicle.getSeatingCapacity() == null || vehicle.getSeatingCapacity() <= 0) {
            throw new IllegalArgumentException("Seating capacity must be greater than zero.");
        }

        if (vehicle.getStatus() == null || !VALID_STATUSES.contains(vehicle.getStatus().trim().toUpperCase())) {
            vehicle.setStatus("AVAILABLE");
        }
    }
}
