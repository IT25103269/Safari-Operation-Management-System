package com.safari.app.vehicle_mgmt;

import com.safari.app.model.MaintenanceLog;
import com.safari.app.model.Vehicle;
import com.safari.app.vehicle_mgmt.dto.MaintenanceRequestDTO;
import com.safari.app.vehicle_mgmt.dto.VehicleRequestDTO;
import com.safari.app.vehicle_mgmt.dto.VehicleStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, MaintenanceLogRepository maintenanceLogRepository) {
        this.vehicleRepository = vehicleRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles(String status) {
        if (status != null && !status.trim().isEmpty()) {
            return vehicleRepository.findByStatus(status.trim().toUpperCase());
        }
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + id));
    }

    public Vehicle registerVehicle(VehicleRequestDTO req) {
        if (vehicleRepository.existsByRegistrationNumber(req.getRegistrationNumber())) {
            throw new IllegalArgumentException("Vehicle with registration number " + req.getRegistrationNumber() + " already exists.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(req.getRegistrationNumber());
        vehicle.setVehicleType(req.getVehicleType());
        vehicle.setCapacity(req.getCapacity());
        vehicle.setModel(req.getModel());
        vehicle.setFuelType(req.getFuelType() != null ? req.getFuelType() : "DIESEL");
        vehicle.setNotes(req.getNotes());
        vehicle.setAssignedDriverName(req.getAssignedDriverName());
        vehicle.setStatus("AVAILABLE");

        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicleStatus(Long id, VehicleStatusDTO statusDto) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setStatus(statusDto.getStatus().toUpperCase());
        if (statusDto.getNotes() != null) {
            vehicle.setNotes(statusDto.getNotes());
        }
        return vehicleRepository.save(vehicle);
    }

    public MaintenanceLog logMaintenanceIssue(Long vehicleId, MaintenanceRequestDTO req) {
        Vehicle vehicle = getVehicleById(vehicleId);

        MaintenanceLog log = new MaintenanceLog();
        log.setIssueDescription(req.getIssueDescription());
        log.setSeverity(req.getSeverity() != null ? req.getSeverity().toUpperCase() : "MEDIUM");
        log.setReportedBy(req.getReportedBy());
        log.setStatus("OPEN");

        vehicle.addMaintenanceLog(log);

        // Auto-toggle vehicle status to MAINTENANCE if high severity
        if ("HIGH".equalsIgnoreCase(req.getSeverity()) || "CRITICAL".equalsIgnoreCase(req.getSeverity())) {
            vehicle.setStatus("MAINTENANCE");
        }

        vehicleRepository.save(vehicle);
        return log;
    }

    @Transactional(readOnly = true)
    public List<MaintenanceLog> getVehicleMaintenanceLogs(Long vehicleId) {
        getVehicleById(vehicleId); // Ensure vehicle exists
        return maintenanceLogRepository.findByVehicleIdOrderByReportedAtDesc(vehicleId);
    }

    public MaintenanceLog resolveMaintenanceIssue(Long logId) {
        MaintenanceLog log = maintenanceLogRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance log not found with ID: " + logId));

        log.setStatus("RESOLVED");
        log.setResolvedAt(LocalDateTime.now());

        Vehicle vehicle = log.getVehicle();
        // Check if all issues resolved for this vehicle
        List<MaintenanceLog> openLogs = maintenanceLogRepository.findByVehicleIdOrderByReportedAtDesc(vehicle.getId())
                .stream().filter(l -> "OPEN".equals(l.getStatus()) || "IN_PROGRESS".equals(l.getStatus()))
                .toList();

        if (openLogs.isEmpty() || openLogs.size() == 1 && openLogs.get(0).getId().equals(logId)) {
            vehicle.setStatus("AVAILABLE");
            vehicleRepository.save(vehicle);
        }

        return maintenanceLogRepository.save(log);
    }


    // --- CRUD: UPDATE vehicle details ---
    public Vehicle updateVehicle(Long id, VehicleRequestDTO req) {
        Vehicle existing = getVehicleById(id);

        if (!existing.getRegistrationNumber().equalsIgnoreCase(req.getRegistrationNumber())) {
            vehicleRepository.findByRegistrationNumber(req.getRegistrationNumber()).ifPresent(other -> {
                if (!other.getId().equals(existing.getId())) {
                    throw new IllegalArgumentException(
                            "Vehicle with registration number " + req.getRegistrationNumber() + " already exists.");
                }
            });
        }

        existing.setRegistrationNumber(req.getRegistrationNumber());
        existing.setVehicleType(req.getVehicleType());
        existing.setCapacity(req.getCapacity());
        existing.setModel(req.getModel());
        existing.setFuelType(req.getFuelType() != null ? req.getFuelType() : "DIESEL");
        existing.setNotes(req.getNotes());
        existing.setAssignedDriverName(req.getAssignedDriverName());

        return vehicleRepository.save(existing);
    }

    // --- CRUD: DELETE vehicle ---
    public void deleteVehicle(Long id) {
        Vehicle existing = getVehicleById(id);
        try {
            vehicleRepository.delete(existing);
            vehicleRepository.flush();
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new IllegalStateException(
                    "Vehicle cannot be deleted because it is referenced by an existing allocation. " +
                            "Change its status to UNAVAILABLE instead.");
        }
    }

}