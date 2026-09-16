package com.safari.app.controller;

import com.safari.app.model.WildlifeSpecies;
import com.safari.app.service.WildlifeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wildlife")
public class WildlifeController {

    private final WildlifeService wildlifeService;

    @Autowired
    public WildlifeController(WildlifeService wildlifeService) {
        this.wildlifeService = wildlifeService;
    }

    @GetMapping
    public ResponseEntity<List<WildlifeSpecies>> getSpecies(
            @RequestParam(required = false) String park,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String query) {
        List<WildlifeSpecies> list = wildlifeService.filterSpecies(park, category, query);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpeciesById(@PathVariable Integer id) {
        return wildlifeService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Species not found with id " + id)));
    }

    @PostMapping
    public ResponseEntity<?> saveSpecies(@RequestBody WildlifeSpecies species) {
        try {
            WildlifeSpecies saved = wildlifeService.saveSpecies(species);
            return ResponseEntity.ok(Map.of("status", "success", "data", saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PostMapping("/archive/{id}")
    public ResponseEntity<?> archiveSpecies(@PathVariable Integer id) {
        try {
            wildlifeService.archiveSpecies(id);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Species archived successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpecies(@PathVariable Integer id) {
        try {
            wildlifeService.deleteSpecies(id);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Species deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
