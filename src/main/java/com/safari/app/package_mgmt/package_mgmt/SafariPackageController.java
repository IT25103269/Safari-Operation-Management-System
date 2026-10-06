package com.safari.app.package_mgmt;

import com.safari.app.cottage_mgmt.dto.ApiResponse;
import com.safari.app.model.SafariPackage;
import com.safari.app.package_mgmt.dto.PackageRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Safari Package Catalog & Admin Management.
 * Base Path: /api/packages/**
 */
@RestController
@RequestMapping({"/api/packages", "/api/safari-packages"})
@CrossOrigin(origins = "*")
public class SafariPackageController {

    private final SafariPackageService packageService;

    @Autowired
    public SafariPackageController(SafariPackageService packageService) {
        this.packageService = packageService;
    }

    // --- PBI-01: Public Filter Endpoint ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<SafariPackage>>> searchPackages(
            @RequestParam(required = false) String park,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "false") boolean includeArchived) {
        List<SafariPackage> packages = packageService.getPackages(park, minPrice, maxPrice, includeArchived);
        return ResponseEntity.ok(ApiResponse.ok("Safari packages fetched successfully", packages));
    }

    // --- PBI-03: Get Package Details with Itinerary ---
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SafariPackage>> getPackageById(@PathVariable Long id) {
        try {
            SafariPackage pkg = packageService.getPackageById(id);
            return ResponseEntity.ok(ApiResponse.ok("Package details retrieved", pkg));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    // --- PBI-02: Tour Coordinator CRUD (Create) ---
    @PostMapping
    public ResponseEntity<ApiResponse<SafariPackage>> createPackage(@Valid @RequestBody PackageRequestDTO req) {
        SafariPackage created = packageService.createPackage(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Safari package created successfully", created));
    }

    // --- PBI-02: Tour Coordinator CRUD (Update) ---
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SafariPackage>> updatePackage(
            @PathVariable Long id, @Valid @RequestBody PackageRequestDTO req) {
        try {
            SafariPackage updated = packageService.updatePackage(id, req);
            return ResponseEntity.ok(ApiResponse.ok("Safari package updated successfully", updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    // --- PBI-02: Archive Package ---
    @PutMapping("/{id}/archive")
    public ResponseEntity<ApiResponse<SafariPackage>> archivePackage(@PathVariable Long id) {
        try {
            SafariPackage archived = packageService.archivePackage(id);
            return ResponseEntity.ok(ApiResponse.ok("Safari package archived successfully", archived));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }


    // --- CRUD: DELETE package ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePackage(@PathVariable Long id) {
        try {
            packageService.deletePackage(id);
            return ResponseEntity.ok(ApiResponse.ok("Safari package deleted successfully", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
        }
    }

}