package com.safari.app.package_mgmt;

import com.safari.app.cottage_mgmt.dto.ApiResponse;
import com.safari.app.model.SafariBooking;
import com.safari.app.package_mgmt.dto.SafariBookingRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** REST API for public safari booking requests and staff management. */
@RestController
@RequestMapping("/api/safari-bookings")
@CrossOrigin(origins = "*")
public class SafariBookingController {

    private final SafariBookingService bookingService;

    public SafariBookingController(SafariBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SafariBooking>> createBooking(
            @Valid @RequestBody SafariBookingRequestDTO req) {
        try {
            SafariBooking created = bookingService.createBooking(req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Safari booking created successfully with reference: " + created.getBookingCode(), created));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SafariBooking>>> getAllBookings(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status) {
        List<SafariBooking> bookings;
        if (email != null && !email.isBlank()) {
            bookings = bookingService.getBookingsByEmail(email);
        } else if (status != null && !status.isBlank()) {
            bookings = bookingService.getBookingsByStatus(status);
        } else {
            bookings = bookingService.getAllBookings();
        }
        return ResponseEntity.ok(ApiResponse.ok("Safari bookings fetched", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SafariBooking>> getBooking(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Safari booking retrieved", bookingService.getBookingById(id)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/reference/{code}")
    public ResponseEntity<ApiResponse<SafariBooking>> getBookingByCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Safari booking retrieved", bookingService.getBookingByCode(code)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SafariBooking>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Safari booking status updated", bookingService.updateStatus(id, status)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.ok(ApiResponse.ok("Safari booking deleted", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }
}
