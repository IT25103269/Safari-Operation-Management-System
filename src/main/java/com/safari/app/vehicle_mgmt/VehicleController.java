package com.safari.app.vehicle_mgmt;

import com.safari.app.cottage_mgmt.dto.ApiResponse;
import com.safari.app.model.MaintenanceLog;
import com.safari.app.model.Vehicle;
import com.safari.app.vehicle_mgmt.dto.MaintenanceRequestDTO;
import com.safari.app.vehicle_mgmt.dto.VehicleRequestDTO;
import com.safari.app.vehicle_mgmt.dto.VehicleStatusDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Safari Fleet & Driver Maintenance Management.
 * Base Path: /api/vehicles/**
 */
@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // --- PBI-04: View Fleet ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<Vehicle>>> getAllVehicles(@RequestParam(required = false) String status) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles(status);
        return ResponseEntity.ok(ApiResponse.ok("Vehicles retrieved successfully", vehicles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> getVehicleById(@PathVariable Long id) {
        try {
            Vehicle vehicle = vehicleService.getVehicleById(id);
            return ResponseEntity.ok(ApiResponse.ok("Vehicle details retrieved", vehicle));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    // --- PBI-04: Register Vehicle ---
    @PostMapping
    public ResponseEntity<ApiResponse<Vehicle>> registerVehicle(@Valid @RequestBody VehicleRequestDTO req) {
        try {
            Vehicle created = vehicleService.registerVehicle(req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Vehicle registered successfully", created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    // --- PBI-05: Update Vehicle Status ---
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Vehicle>> updateStatus(
            @PathVariable Long id, @Valid @RequestBody VehicleStatusDTO statusDto) {
        try {
            Vehicle updated = vehicleService.updateVehicleStatus(id, statusDto);
            return ResponseEntity.ok(ApiResponse.ok("Vehicle status updated to " + updated.getStatus(), updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    // --- PBI-06: Report Vehicle Maintenance Issue ---
    @PostMapping("/{id}/maintenance")
    public ResponseEntity<ApiResponse<MaintenanceLog>> reportMaintenance(
            @PathVariable Long id, @Valid @RequestBody MaintenanceRequestDTO req) {
        try {
            MaintenanceLog log = vehicleService.logMaintenanceIssue(id, req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Maintenance issue logged successfully", log));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/{id}/maintenance")
    public ResponseEntity<ApiResponse<List<MaintenanceLog>>> getMaintenanceLogs(@PathVariable Long id) {
        try {
            List<MaintenanceLog> logs = vehicleService.getVehicleMaintenanceLogs(id);
            return ResponseEntity.ok(ApiResponse.ok("Maintenance history retrieved", logs));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/maintenance/{logId}/resolve")
    public ResponseEntity<ApiResponse<MaintenanceLog>> resolveMaintenance(@PathVariable Long logId) {
        try {
            MaintenanceLog resolved = vehicleService.resolveMaintenanceIssue(logId);
            return ResponseEntity.ok(ApiResponse.ok("Maintenance issue marked as resolved", resolved));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }


    // --- CRUD: UPDATE vehicle details ---
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> updateVehicle(
            @PathVariable Long id, @Valid @RequestBody VehicleRequestDTO req) {
        try {
            Vehicle updated = vehicleService.updateVehicle(id, req);
            return ResponseEntity.ok(ApiResponse.ok("Vehicle updated successfully", updated));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    // --- CRUD: DELETE vehicle ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.ok(ApiResponse.ok("Vehicle deleted successfully", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
        }
    }

}