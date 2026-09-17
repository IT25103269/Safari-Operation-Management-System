package com.safari.app.controller;

import com.safari.app.model.WildlifeSighting;
import com.safari.app.service.WildlifeSightingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class WildlifeSightingController {

    @Autowired
    private WildlifeSightingService sightingService;

    @GetMapping("/api/sightings")
    public ResponseEntity<List<WildlifeSighting>> getSightings(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(sightingService.getAllSightings(status));
    }

    @PostMapping("/api/sightings")
    public ResponseEntity<?> logSightingJson(@RequestBody WildlifeSighting sighting) {
        try {
            WildlifeSighting saved = sightingService.logSighting(sighting);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/submit-sighting")
    public void handleSubmitSightingForm(
            @RequestParam("tripId") String tripId,
            @RequestParam("animalName") String animalName,
            @RequestParam("sightingDate") String sightingDateStr,
            @RequestParam("sightingTime") String sightingTime,
            @RequestParam("park") String park,
            @RequestParam("location") String location,
            @RequestParam(value = "notes", required = false) String notes,
            HttpServletResponse response) throws IOException {

        try {
            WildlifeSighting sighting = new WildlifeSighting();
            sighting.setTripRef(tripId);
            sighting.setSpeciesName(animalName);
            sighting.setSightingDate(LocalDate.parse(sightingDateStr));
            sighting.setSightingTime(sightingTime);
            sighting.setParkLocation(park);
            sighting.setSpecificLocation(location);
            sighting.setNotes(notes);
            sighting.setLoggedBy("Field Guide / Driver");
            sighting.setStatus("Pending");

            sightingService.logSighting(sighting);
            response.sendRedirect("/view-sightings-review.html?submitted=true");
        } catch (Exception e) {
            response.sendRedirect("/log-sighting.html?error=" + e.getMessage());
        }
    }

    @PutMapping("/api/sightings/{id}/status")
    public ResponseEntity<?> updateSightingStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload) {
        try {
            String status = payload.get("status");
            WildlifeSighting updated = sightingService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
