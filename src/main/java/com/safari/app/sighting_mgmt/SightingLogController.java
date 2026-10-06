package com.safari.app.sighting_mgmt;

import com.safari.app.cottage_mgmt.dto.ApiResponse;
import com.safari.app.model.SightingLog;
import com.safari.app.sighting_mgmt.dto.SightingRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sightings")
@CrossOrigin(origins = "*")
public class SightingLogController {

    private final SightingLogService sightingLogService;

    @Autowired
    public SightingLogController(SightingLogService sightingLogService) {
        this.sightingLogService = sightingLogService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SightingLog>> logSighting(@Valid @RequestBody SightingRequestDTO req) {
        try {
            SightingLog log = sightingLogService.logSighting(req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Wildlife sighting logged successfully", log));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SightingLog>>> getAllSightings(
            @RequestParam(required = false) Long speciesId) {
        List<SightingLog> sightings;
        if (speciesId != null) {
            sightings = sightingLogService.getSightingsBySpecies(speciesId);
        } else {
            sightings = sightingLogService.getAllSightings();
        }
        return ResponseEntity.ok(ApiResponse.ok("Sighting logs retrieved", sightings));
    }


    //UPDATE sighting
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SightingLog>> updateSighting(
            @PathVariable Long id, @Valid @RequestBody SightingRequestDTO req) {
        try {
            SightingLog updated = sightingLogService.updateSighting(id, req);
            return ResponseEntity.ok(ApiResponse.ok("Sighting updated successfully", updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    //DELETE sighting
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSighting(@PathVariable Long id) {
        try {
            sightingLogService.deleteSighting(id);
            return ResponseEntity.ok(ApiResponse.ok("Sighting deleted successfully", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

}