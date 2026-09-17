package com.safari.app.repository;

import com.safari.app.model.VehicleMaintenanceIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleMaintenanceRepository extends JpaRepository<VehicleMaintenanceIssue, Long> {
    List<VehicleMaintenanceIssue> findByVehicleIdOrderByReportedAtDesc(Long vehicleId);
}
