package com.safari.app.vehicle_mgmt;

import com.safari.app.model.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {

    List<MaintenanceLog> findByVehicleIdOrderByReportedAtDesc(Long vehicleId);

    List<MaintenanceLog> findByStatus(String status);
}