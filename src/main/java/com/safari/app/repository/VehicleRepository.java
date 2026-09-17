package com.safari.app.repository;

import com.safari.app.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByRegistrationNumberIgnoreCase(String registrationNumber);
    boolean existsByRegistrationNumberIgnoreCase(String registrationNumber);
    boolean existsByRegistrationNumberIgnoreCaseAndIdNot(String registrationNumber, Long id);
    List<Vehicle> findByStatus(String status);
    List<Vehicle> findByStatusIgnoreCaseAndSeatingCapacityGreaterThanEqual(String status, Integer seatingCapacity);
    List<Vehicle> findByAssignedDriverIgnoreCase(String assignedDriver);
}
