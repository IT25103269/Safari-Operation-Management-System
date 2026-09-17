package com.safari.app.service;

import com.safari.app.model.WildlifeSighting;
import com.safari.app.repository.WildlifeSightingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WildlifeSightingService {

    @Autowired
    private WildlifeSightingRepository sightingRepository;

    public List<WildlifeSighting> getAllSightings(String status) {
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
            return sightingRepository.findByStatusIgnoreCaseOrderByCreatedAtDesc(status.trim());
        }
        return sightingRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<WildlifeSighting> getVerifiedSightings() {
        return sightingRepository.findByStatusIgnoreCaseOrderByCreatedAtDesc("Verified");
    }

    public WildlifeSighting logSighting(WildlifeSighting sighting) {
        if (sighting.getSpeciesName() == null || sighting.getSpeciesName().trim().isEmpty()) {
            throw new IllegalArgumentException("Species name is required.");
        }
        if (sighting.getParkLocation() == null || sighting.getParkLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("National park location is required.");
        }
        if (sighting.getSightingDate() == null) {
            sighting.setSightingDate(LocalDate.now());
        }
        if (sighting.getSightingTime() == null || sighting.getSightingTime().trim().isEmpty()) {
            sighting.setSightingTime("Morning");
        }
        if (sighting.getLoggedBy() == null || sighting.getLoggedBy().trim().isEmpty()) {
            sighting.setLoggedBy("Safari Field Guide");
        }
        if (sighting.getStatus() == null || sighting.getStatus().trim().isEmpty()) {
            sighting.setStatus("Pending");
        }

        return sightingRepository.save(sighting);
    }

    public WildlifeSighting updateStatus(Integer id, String newStatus) {
        WildlifeSighting sighting = sightingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sighting not found with ID: " + id));

        if (!"Verified".equalsIgnoreCase(newStatus) && !"Rejected".equalsIgnoreCase(newStatus) && !"Pending".equalsIgnoreCase(newStatus)) {
            throw new IllegalArgumentException("Invalid sighting status: " + newStatus);
        }

        sighting.setStatus(newStatus.substring(0, 1).toUpperCase() + newStatus.substring(1).toLowerCase());
        return sightingRepository.save(sighting);
    }
}
