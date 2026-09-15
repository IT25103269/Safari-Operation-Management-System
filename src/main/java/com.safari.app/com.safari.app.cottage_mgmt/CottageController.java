package com.safari.app.cottage_mgmt;

import com.safari.app.cottage_mgmt.dto.*;
import com.safari.app.model.Cottage;
import com.safari.app.model.CottageReservation;
import com.safari.app.model.FineRecord;
import com.safari.app.model.GuestLog;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing Cottage Reservation Subsystem endpoints.
 * Base Path: /api/cottages/**
 * 
 * Student: Mehthab M.M. (IT25101495) - SE2030 Software Engineering
 */
@RestController
@RequestMapping("/api/cottages")
@CrossOrigin(origins = "*")
public class CottageController {

    private final CottageService cottageService;

    @Autowired
    public CottageController(CottageService cottageService) {
        this.cottageService = cottageService;
    }

    // ==========================================
    // 1. COTTAGE CATALOGUE & AVAILABILITY
    // ==========================================

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cottage>>> getAllCottages() {
        List<Cottage> cottages = cottageService.getAllCottages();
        return ResponseEntity.ok(ApiResponse.ok("Cottages fetched successfully", cottages));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cottage>> getCottageById(@PathVariable Long id) {
        Cottage cottage = cottageService.getCottageById(id);
        return ResponseEntity.ok(ApiResponse.ok("Cottage retrieved", cottage));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cottage>> createCottage(@Valid @RequestBody Cottage cottage) {
        Cottage created = cottageService.createCottage(cottage);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cottage registered successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cottage>> updateCottage(@PathVariable Long id, @RequestBody Cottage cottage) {
        Cottage updated = cottageService.updateCottage(id, cottage);
        return ResponseEntity.ok(ApiResponse.ok("Cottage updated successfully", updated));
    }

    /**
     * Real-time availability endpoint (PBI-10 / T-04.1 - T-04.4)
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Cottage>>> getAvailableCottages(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) Integer guests,
            @RequestParam(required = false) String type) {
        try {
            List<Cottage> available = cottageService.findAvailableCottages(checkIn, checkOut, guests, type);
            return ResponseEntity.ok(ApiResponse.ok("Available cottages retrieved for requested dates", available));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    // ==========================================
    // 2. RESERVATION STATE MANAGEMENT (PBI-11)
    // ==========================================

    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse<CottageReservation>> createReservation(@Valid @RequestBody ReservationRequestDTO req) {
        try {
            CottageReservation res = cottageService.createReservation(req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Reservation created successfully with code: " + res.getReservationCode(), res));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<ApiResponse<List<CottageReservation>>> getAllReservations(
            @RequestParam(required = false) String status) {
        List<CottageReservation> list = cottageService.getAllReservations(status);
        return ResponseEntity.ok(ApiResponse.ok("Reservations fetched", list));
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ApiResponse<CottageReservation>> getReservationById(@PathVariable Long id) {
        try {
            CottageReservation res = cottageService.getReservationById(id);
            return ResponseEntity.ok(ApiResponse.ok("Reservation retrieved", res));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/reservations/{id}/approve")
    public ResponseEntity<ApiResponse<CottageReservation>> approveReservation(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String notes = body != null ? body.get("notes") : null;
            String approver = body != null ? body.get("approver") : "Cottage Manager";
            CottageReservation res = cottageService.approveReservation(id, notes, approver);
            return ResponseEntity.ok(ApiResponse.ok("Reservation approved successfully", res));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/reservations/{id}/reject")
    public ResponseEntity<ApiResponse<CottageReservation>> rejectReservation(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String reason = body != null && body.containsKey("reason") ? body.get("reason") : "Capacity constraints / Date unavailability";
            String rejecter = body != null ? body.get("rejecter") : "Cottage Manager";
            CottageReservation res = cottageService.rejectReservation(id, reason, rejecter);
            return ResponseEntity.ok(ApiResponse.ok("Reservation rejected and refund processed", res));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/reservations/{id}/cancel")
    public ResponseEntity<ApiResponse<CottageReservation>> cancelReservation(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null && body.containsKey("reason") ? body.get("reason") : "Guest requested cancellation";
            CottageReservation res = cottageService.cancelReservation(id, reason);
            return ResponseEntity.ok(ApiResponse.ok("Reservation cancelled and refund processed: $" + res.getRefundAmount(), res));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    // ==========================================
    // 3. CHECK-IN/OUT & PENALTY ASSESSMENT (PBI-12)
    // ==========================================

    @PostMapping("/reservations/{id}/check-in")
    public ResponseEntity<ApiResponse<GuestLog>> checkInGuest(
            @PathVariable Long id,
            @RequestBody(required = false) CheckInRequestDTO req) {
        try {
            CheckInRequestDTO dto = (req != null) ? req : new CheckInRequestDTO();
            GuestLog log = cottageService.checkInGuest(id, dto);
            return ResponseEntity.ok(ApiResponse.ok("Guest checked in successfully. Keycard: " + log.getKeyCardNumber(), log));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/reservations/{id}/check-out")
    public ResponseEntity<ApiResponse<GuestLog>> checkOutGuest(
            @PathVariable Long id,
            @RequestBody(required = false) CheckOutRequestDTO req) {
        try {
            CheckOutRequestDTO dto = (req != null) ? req : new CheckOutRequestDTO();
            GuestLog log = cottageService.checkOutGuest(id, dto);
            return ResponseEntity.ok(ApiResponse.ok("Guest checked out successfully. Penalties evaluated.", log));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/reservations/{id}/fines")
    public ResponseEntity<ApiResponse<List<FineRecord>>> getReservationFines(@PathVariable Long id) {
        List<FineRecord> fines = cottageService.getFinesByReservation(id);
        return ResponseEntity.ok(ApiResponse.ok("Fines retrieved", fines));
    }

    @PostMapping("/reservations/{id}/fines")
    public ResponseEntity<ApiResponse<FineRecord>> addManualFine(
            @PathVariable Long id,
            @Valid @RequestBody FineRequestDTO req) {
        try {
            FineRecord fine = cottageService.addManualFine(id, req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Fine assessed successfully", fine));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/fines/{fineId}/pay")
    public ResponseEntity<ApiResponse<FineRecord>> payFine(@PathVariable Long fineId) {
        try {
            FineRecord paid = cottageService.payFine(fineId);
            return ResponseEntity.ok(ApiResponse.ok("Fine settled and marked as PAID", paid));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<List<GuestLog>>> getGuestLogs(
            @RequestParam(required = false) Long reservationId) {
        List<GuestLog> logs = cottageService.getGuestLogs(reservationId);
        return ResponseEntity.ok(ApiResponse.ok("Guest logs retrieved", logs));
    }

    // ==========================================
    // 4. OCCUPANCY & ANALYTICAL REPORTS
    // ==========================================

    @GetMapping("/reports/occupancy")
    public ResponseEntity<ApiResponse<OccupancyReportDTO>> getOccupancyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        OccupancyReportDTO report = cottageService.generateOccupancyReport(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok("Occupancy report generated successfully", report));
    }
}