package com.safari.app.controller;

import com.safari.app.model.SafariPackage;
import com.safari.app.service.SafariPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/packages")
public class SafariPackageController {

    @Autowired
    private SafariPackageService packageService;

    @GetMapping
    public ResponseEntity<List<SafariPackage>> getAllPackages(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean activeOnly) {
        return ResponseEntity.ok(packageService.getAllPackages(destination, maxPrice, query, activeOnly));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable Integer id) {
        try {
            SafariPackage pkg = packageService.getPackageById(id);
            return ResponseEntity.ok(pkg);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPackage(@RequestBody SafariPackage safariPackage) {
        try {
            SafariPackage saved = packageService.createPackage(safariPackage);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackage(@PathVariable Integer id, @RequestBody SafariPackage safariPackage) {
        try {
            SafariPackage updated = packageService.updatePackage(id, safariPackage);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<?> toggleArchive(@PathVariable Integer id) {
        try {
            SafariPackage updated = packageService.toggleArchive(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Integer id) {
        try {
            packageService.deletePackage(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Package deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
