package com.safari.app.controller;

import com.safari.app.model.SafariBooking;
import com.safari.app.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DriverGuideTripController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/driver/allocations")
    public ResponseEntity<List<Map<String, Object>>> getDriverAllocations(HttpSession session) {
        String loggedInEmail = session != null ? (String) session.getAttribute("LOGGED_IN_USER_EMAIL") : null;
        List<SafariBooking> bookings = bookingService.getAllBookings();

        // Map to format expected by driver-dashboard.html:
        // tripId, destination, vehicleRegNumber, scheduledDateTime, guideName, status
        List<Map<String, Object>> result = bookings.stream()
                .filter(b -> !"CANCELLED".equalsIgnoreCase(b.getStatus()))
                .map(b -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("tripId", b.getBookingRef());
                    map.put("destination", b.getDestination() + " - " + b.getPackageName());
                    map.put("vehicleRegNumber", b.getAssignedVehicle() != null ? b.getAssignedVehicle() : "Unassigned");
                    map.put("scheduledDateTime", b.getTravelDate().toString() + " (Full Day)");
                    map.put("guideName", b.getAssignedGuide() != null ? b.getAssignedGuide() : "Unassigned");
                    map.put("status", b.getStatus());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/guide/allocations")
    public ResponseEntity<List<SafariBooking>> getGuideAllocations(HttpSession session) {
        List<SafariBooking> bookings = bookingService.getAllBookings();
        List<SafariBooking> active = bookings.stream()
                .filter(b -> !"CANCELLED".equalsIgnoreCase(b.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(active);
    }
}
