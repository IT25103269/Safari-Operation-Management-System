package com.safari.app.controller;

import com.safari.app.model.SafariBooking;
import com.safari.app.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody SafariBooking booking, HttpSession session) {
        try {
            SafariBooking created = bookingService.createBooking(booking, session);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<SafariBooking>> getMyBookings(HttpSession session) {
        return ResponseEntity.ok(bookingService.getMyBookings(session));
    }

    @GetMapping
    public ResponseEntity<List<SafariBooking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(bookingService.getBookingById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        try {
            String status = payload.get("status");
            return ResponseEntity.ok(bookingService.updateStatus(id, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(bookingService.cancelBooking(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/allocate")
    public ResponseEntity<?> allocateResources(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload) {
        try {
            String guide = payload.get("guide");
            String vehicle = payload.get("vehicle");
            SafariBooking updated = bookingService.allocateResources(id, guide, vehicle);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
