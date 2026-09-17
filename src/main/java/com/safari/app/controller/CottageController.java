package com.safari.app.controller;

import com.safari.app.model.Cottage;
import com.safari.app.model.CottageReservation;
import com.safari.app.service.CottageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cottages")
public class CottageController {

    private final CottageService cottageService;

    @Autowired
    public CottageController(CottageService cottageService) {
        this.cottageService = cottageService;
    }

    private Map<String, Object> successResponse(String message, Object data) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", message);
        resp.put("data", data);
        return resp;
    }

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", false);
        resp.put("message", message);
        return resp;
    }

    @GetMapping
    public ResponseEntity<?> getAllCottages() {
        List<Cottage> list = cottageService.getAllCottages();
        return ResponseEntity.ok(successResponse("Cottages retrieved successfully", list));
    }

    @PostMapping
    public ResponseEntity<?> addCottage(@RequestBody Cottage cottage) {
        try {
            Cottage saved = cottageService.saveCottage(cottage);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(successResponse("Cottage added successfully", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCottage(@PathVariable Integer id) {
        try {
            cottageService.deleteCottage(id);
            return ResponseEntity.ok(successResponse("Cottage deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getAllReservations() {
        List<CottageReservation> list = cottageService.getAllReservations();
        return ResponseEntity.ok(successResponse("Reservations retrieved successfully", list));
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> payload) {
        try {
            Integer cottageId = payload.get("cottageId") != null ? Integer.parseInt(payload.get("cottageId").toString()) : null;
            if (cottageId == null) {
                return ResponseEntity.badRequest().body(errorResponse("Cottage ID is required"));
            }

            CottageReservation res = new CottageReservation();
            res.setTouristName((String) payload.getOrDefault("guestName", payload.get("touristName")));
            res.setTouristEmail((String) payload.getOrDefault("guestEmail", payload.get("touristEmail")));
            res.setTouristPhone((String) payload.getOrDefault("guestPhone", payload.get("touristPhone")));

            String inDateStr = (String) payload.get("checkInDate");
            String outDateStr = (String) payload.get("checkOutDate");
            if (inDateStr != null) res.setCheckInDate(LocalDate.parse(inDateStr));
            if (outDateStr != null) res.setCheckOutDate(LocalDate.parse(outDateStr));

            if (payload.get("numberOfGuests") != null) {
                res.setGuestsCount(Integer.parseInt(payload.get("numberOfGuests").toString()));
            } else if (payload.get("guestsCount") != null) {
                res.setGuestsCount(Integer.parseInt(payload.get("guestsCount").toString()));
            }

            if (payload.get("extraBeds") != null) {
                res.setExtraBeds(Integer.parseInt(payload.get("extraBeds").toString()));
            }

            res.setSpecialRequests((String) payload.get("specialRequests"));

            CottageReservation saved = cottageService.createReservation(res, cottageId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(successResponse("Reservation created successfully", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PutMapping("/reservations/{id}/approve")
    public ResponseEntity<?> approveReservation(@PathVariable Integer id) {
        try {
            CottageReservation updated = cottageService.approveReservation(id);
            return ResponseEntity.ok(successResponse("Reservation approved successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PutMapping("/reservations/{id}/reject")
    public ResponseEntity<?> rejectReservation(@PathVariable Integer id) {
        try {
            CottageReservation updated = cottageService.rejectReservation(id);
            return ResponseEntity.ok(successResponse("Reservation rejected successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/reservations/{id}/check-in")
    public ResponseEntity<?> checkInReservation(@PathVariable Integer id) {
        try {
            CottageReservation updated = cottageService.checkInReservation(id);
            return ResponseEntity.ok(successResponse("Reservation checked in successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/reservations/{id}/check-out")
    public ResponseEntity<?> checkOutReservation(@PathVariable Integer id) {
        try {
            CottageReservation updated = cottageService.checkOutReservation(id);
            return ResponseEntity.ok(successResponse("Reservation checked out successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }
}
