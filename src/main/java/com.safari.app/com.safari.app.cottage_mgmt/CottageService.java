package com.safari.app.cottage_mgmt;

import com.safari.app.cottage_mgmt.dto.*;
import com.safari.app.model.Cottage;
import com.safari.app.model.CottageReservation;
import com.safari.app.model.FineRecord;
import com.safari.app.model.GuestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Core business service for Cottage Reservation Management.
 * Implements Real-Time Availability, Reservation Lifecycle, Check-In/Out & Penalty Assessment,
 * and Analytical Occupancy Reporting.
 * 
 * Student: Mehthab M.M. (IT25101495) - SE2030 Software Engineering
 */
@Service
@Transactional
public class CottageService {

    public static final BigDecimal EXTRA_BED_RATE_PER_NIGHT = new BigDecimal("25.00");
    public static final BigDecimal HOURLY_OVERSTAY_RATE = new BigDecimal("20.00");
    public static final int STANDARD_CHECKOUT_HOUR = 11;
    public static final int OVERSTAY_GRACE_MINUTES = 30;

    private final CottageRepository cottageRepository;
    private final ReservationRepository reservationRepository;
    private final GuestLogRepository guestLogRepository;
    private final FineRecordRepository fineRecordRepository;

    @Autowired
    public CottageService(CottageRepository cottageRepository,
                          ReservationRepository reservationRepository,
                          GuestLogRepository guestLogRepository,
                          FineRecordRepository fineRecordRepository) {
        this.cottageRepository = cottageRepository;
        this.reservationRepository = reservationRepository;
        this.guestLogRepository = guestLogRepository;
        this.fineRecordRepository = fineRecordRepository;
    }

    // ==========================================
    // 1. REAL-TIME AVAILABILITY & COTTAGE MGMT (PBI-10)
    // ==========================================

    @Transactional(readOnly = true)
    public List<Cottage> findAvailableCottages(LocalDate checkIn, LocalDate checkOut, Integer guests, String type) {
        validateDateRange(checkIn, checkOut);
        int guestCount = (guests != null && guests > 0) ? guests : 1;
        String cottageType = (type != null && !type.trim().isEmpty()) ? type.trim().toUpperCase() : null;

        return cottageRepository.findAvailableCottagesWithFilter(checkIn, checkOut, guestCount, cottageType);
    }

    @Transactional(readOnly = true)
    public List<Cottage> getAllCottages() {
        return cottageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cottage getCottageById(Long id) {
        return cottageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cottage not found with ID: " + id));
    }

    public Cottage createCottage(Cottage cottage) {
        if (cottage.getCottageNumber() == null || cottage.getCottageNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Cottage number is required");
        }
        if (cottageRepository.findByCottageNumber(cottage.getCottageNumber()).isPresent()) {
            throw new IllegalArgumentException("Cottage number already exists: " + cottage.getCottageNumber());
        }
        return cottageRepository.save(cottage);
    }

    public Cottage updateCottage(Long id, Cottage updated) {
        Cottage existing = getCottageById(id);
        existing.setCottageName(updated.getCottageName());
        existing.setCottageType(updated.getCottageType());
        existing.setBasePricePerNight(updated.getBasePricePerNight());
        existing.setMaxOccupancy(updated.getMaxOccupancy());
        existing.setDescription(updated.getDescription());
        existing.setAmenities(updated.getAmenities());
        existing.setStatus(updated.getStatus());
        return cottageRepository.save(existing);
    }

    // ==========================================
    // 2. RESERVATION STATE MANAGEMENT & APPROVALS (PBI-11)
    // ==========================================

    public CottageReservation createReservation(ReservationRequestDTO req) {
        validateDateRange(req.getCheckInDate(), req.getCheckOutDate());

        Cottage cottage = getCottageById(req.getCottageId());

        if (!"AVAILABLE".equalsIgnoreCase(cottage.getStatus())) {
            throw new IllegalStateException("Cottage " + cottage.getCottageNumber() + " is currently not available for booking (Status: " + cottage.getStatus() + ")");
        }

        int extraBeds = req.getExtraBeds() != null ? req.getExtraBeds() : 0;
        int totalCapacity = cottage.getMaxOccupancy() + extraBeds;
        if (req.getNumberOfGuests() > totalCapacity) {
            throw new IllegalArgumentException("Number of guests (" + req.getNumberOfGuests() + 
                    ") exceeds cottage maximum capacity with extra beds (" + totalCapacity + ")");
        }

        // Overlap verification
        long conflicts = reservationRepository.countConflictingReservations(
                cottage.getId(), req.getCheckInDate(), req.getCheckOutDate(), null);
        if (conflicts > 0) {
            throw new IllegalStateException("Selected cottage is already booked for the requested dates");
        }

        long nights = ChronoUnit.DAYS.between(req.getCheckInDate(), req.getCheckOutDate());
        BigDecimal baseAmount = cottage.getBasePricePerNight().multiply(BigDecimal.valueOf(nights));
        BigDecimal extraBedFee = EXTRA_BED_RATE_PER_NIGHT
                .multiply(BigDecimal.valueOf(extraBeds))
                .multiply(BigDecimal.valueOf(nights));
        BigDecimal totalAmount = baseAmount.add(extraBedFee);

        CottageReservation res = new CottageReservation();
        res.setReservationCode(generateReservationCode(req.getCheckInDate()));
        res.setCottage(cottage);
        res.setUserId(req.getUserId());
        res.setGuestName(req.getGuestName());
        res.setGuestEmail(req.getGuestEmail());
        res.setGuestPhone(req.getGuestPhone());
        res.setCheckInDate(req.getCheckInDate());
        res.setCheckOutDate(req.getCheckOutDate());
        res.setNumberOfGuests(req.getNumberOfGuests());
        res.setExtraBeds(extraBeds);
        res.setSpecialRequests(req.getSpecialRequests());
        res.setBaseAmount(baseAmount);
        res.setExtraBedFee(extraBedFee);
        res.setTotalAmount(totalAmount);
        res.setPaidAmount(totalAmount); // Simulated instant confirmation payment or deposit
        res.setStatus("PENDING");

        return reservationRepository.save(res);
    }

    public CottageReservation approveReservation(Long reservationId, String managerNotes, String approver) {
        CottageReservation res = getReservationById(reservationId);

        if (!"PENDING".equalsIgnoreCase(res.getStatus())) {
            throw new IllegalStateException("Only reservations in PENDING status can be approved. Current status: " + res.getStatus());
        }

        // Ensure no other reservation was confirmed in the interim
        long conflicts = reservationRepository.countConflictingReservations(
                res.getCottage().getId(), res.getCheckInDate(), res.getCheckOutDate(), res.getId());
        if (conflicts > 0) {
            throw new IllegalStateException("Cannot approve reservation: conflicting confirmed booking exists for these dates");
        }

        res.setStatus("CONFIRMED");
        String notes = "Approved by " + (approver != null ? approver : "Manager");
        if (managerNotes != null && !managerNotes.trim().isEmpty()) {
            notes += " - " + managerNotes.trim();
        }
        res.setManagerNotes(notes);

        return reservationRepository.save(res);
    }

    public CottageReservation rejectReservation(Long reservationId, String reason, String rejecter) {
        CottageReservation res = getReservationById(reservationId);

        if (!"PENDING".equalsIgnoreCase(res.getStatus())) {
            throw new IllegalStateException("Only reservations in PENDING status can be rejected. Current status: " + res.getStatus());
        }

        res.setStatus("REJECTED");
        res.setCancellationReason(reason);
        res.setManagerNotes("Rejected by " + (rejecter != null ? rejecter : "Manager") + ": " + reason);
        res.setRefundAmount(res.getPaidAmount()); // 100% refund on rejection

        return reservationRepository.save(res);
    }

    public CottageReservation cancelReservation(Long reservationId, String cancellationReason) {
        CottageReservation res = getReservationById(reservationId);

        if ("CANCELLED".equalsIgnoreCase(res.getStatus()) || "REJECTED".equalsIgnoreCase(res.getStatus()) || "CHECKED_OUT".equalsIgnoreCase(res.getStatus())) {
            throw new IllegalStateException("Reservation cannot be cancelled from current status: " + res.getStatus());
        }

        // Tiered cancellation refund policy:
        // >= 7 days: 100% refund
        // 3 to 6 days: 70% refund (30% retention penalty)
        // 1 to 2 days: 50% refund (50% retention penalty)
        // Same day / past: 0% refund
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), res.getCheckInDate());
        BigDecimal refundMultiplier;

        if (daysUntilCheckIn >= 7) {
            refundMultiplier = new BigDecimal("1.00");
        } else if (daysUntilCheckIn >= 3) {
            refundMultiplier = new BigDecimal("0.70");
        } else if (daysUntilCheckIn >= 1) {
            refundMultiplier = new BigDecimal("0.50");
        } else {
            refundMultiplier = BigDecimal.ZERO;
        }

        BigDecimal refundAmount = res.getPaidAmount().multiply(refundMultiplier).setScale(2, RoundingMode.HALF_UP);

        res.setStatus("CANCELLED");
        res.setCancellationReason(cancellationReason);
        res.setRefundAmount(refundAmount);
        res.setManagerNotes("Cancelled " + daysUntilCheckIn + " days before check-in. Refund calculated at " + 
                refundMultiplier.multiply(new BigDecimal("100")).intValue() + "% ($" + refundAmount + ")");

        // Release cottage status if occupied
        Cottage cottage = res.getCottage();
        if ("OCCUPIED".equalsIgnoreCase(cottage.getStatus())) {
            cottage.setStatus("AVAILABLE");
            cottageRepository.save(cottage);
        }

        return reservationRepository.save(res);
    }

    @Transactional(readOnly = true)
    public List<CottageReservation> getAllReservations(String status) {
        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            return reservationRepository.findByStatus(status.trim().toUpperCase());
        }
        return reservationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public CottageReservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));
    }

    // ==========================================
    // 3. GUEST CHECK-IN/OUT & PENALTY ASSESSMENT (PBI-12)
    // ==========================================

    public GuestLog checkInGuest(Long reservationId, CheckInRequestDTO req) {
        CottageReservation res = getReservationById(reservationId);

        if (!"CONFIRMED".equalsIgnoreCase(res.getStatus()) && !"PENDING".equalsIgnoreCase(res.getStatus())) {
            throw new IllegalStateException("Reservation must be CONFIRMED to check-in. Current status: " + res.getStatus());
        }

        res.setStatus("CHECKED_IN");
        Cottage cottage = res.getCottage();
        cottage.setStatus("OCCUPIED");
        cottageRepository.save(cottage);
        reservationRepository.save(res);

        GuestLog log = new GuestLog();
        log.setReservation(res);
        log.setActualCheckInTime(LocalDateTime.now());
        log.setScheduledCheckOutTime(res.getCheckOutDate().atTime(STANDARD_CHECKOUT_HOUR, 0));
        log.setKeyCardNumber(req.getKeyCardNumber() != null ? req.getKeyCardNumber() : "KEY-" + res.getCottage().getCottageNumber());
        log.setNotes(req.getNotes());
        log.setLogStatus("CHECKED_IN");

        return guestLogRepository.save(log);
    }

    public GuestLog checkOutGuest(Long reservationId, CheckOutRequestDTO req) {
        CottageReservation res = getReservationById(reservationId);

        if (!"CHECKED_IN".equalsIgnoreCase(res.getStatus())) {
            throw new IllegalStateException("Only CHECKED_IN reservations can be checked out. Current status: " + res.getStatus());
        }

        GuestLog log = guestLogRepository.findTopByReservationIdOrderByActualCheckInTimeDesc(reservationId)
                .orElseThrow(() -> new IllegalStateException("No active guest check-in log found for reservation: " + reservationId));

        LocalDateTime actualCheckOut = LocalDateTime.now();
        log.setActualCheckOutTime(actualCheckOut);
        log.setLogStatus("COMPLETED");

        if (req.getNotes() != null) {
            String combinedNotes = (log.getNotes() != null ? log.getNotes() + "; " : "") + req.getNotes();
            log.setNotes(combinedNotes);
        }

        // 1. Automated Overstay Penalty Calculation
        LocalDateTime scheduledCheckOut = log.getScheduledCheckOutTime();
        LocalDateTime graceThreshold = scheduledCheckOut.plusMinutes(OVERSTAY_GRACE_MINUTES);

        if (actualCheckOut.isAfter(graceThreshold)) {
            long overstayMinutes = Duration.between(scheduledCheckOut, actualCheckOut).toMinutes();
            long billableHours = (long) Math.ceil(overstayMinutes / 60.0);
            BigDecimal overstayFee = HOURLY_OVERSTAY_RATE.multiply(BigDecimal.valueOf(billableHours)).setScale(2, RoundingMode.HALF_UP);

            log.setOverstayMinutes((int) overstayMinutes);
            log.setOverstayFee(overstayFee);

            // Create FineRecord for overstay
            FineRecord overstayFine = new FineRecord(
                    res,
                    log,
                    "OVERSTAY",
                    overstayFee,
                    "Late check-out penalty: " + overstayMinutes + " minutes overstay beyond standard 11:00 AM check-out (" + billableHours + " billable hours @ $" + HOURLY_RATE_STRING() + "/hr)",
                    "Automated Penalty Engine"
            );
            fineRecordRepository.save(overstayFine);
        }

        // 2. Damage Assessment Penalty
        if (req.getDamageAssessed() != null && req.getDamageAssessed() && req.getDamageFee() != null && req.getDamageFee().compareTo(BigDecimal.ZERO) > 0) {
            log.setDamageAssessed(true);
            log.setDamageDescription(req.getDamageDescription());
            log.setDamageFee(req.getDamageFee());

            FineRecord damageFine = new FineRecord(
                    res,
                    log,
                    "ROOM_DAMAGE",
                    req.getDamageFee(),
                    "Room damage penalty assessed: " + (req.getDamageDescription() != null ? req.getDamageDescription() : "Damaged cottage equipment/furniture"),
                    "Cottage Inspector"
            );
            fineRecordRepository.save(damageFine);
        }

        // Update reservation and cottage status
        res.setStatus("CHECKED_OUT");
        Cottage cottage = res.getCottage();
        cottage.setStatus("AVAILABLE");

        cottageRepository.save(cottage);
        reservationRepository.save(res);
        return guestLogRepository.save(log);
    }

    public FineRecord addManualFine(Long reservationId, FineRequestDTO req) {
        CottageReservation res = getReservationById(reservationId);
        GuestLog log = guestLogRepository.findTopByReservationIdOrderByActualCheckInTimeDesc(reservationId).orElse(null);

        FineRecord fine = new FineRecord(
                res,
                log,
                req.getFineType().toUpperCase(),
                req.getFineAmount(),
                req.getReason(),
                req.getAssessedBy() != null ? req.getAssessedBy() : "Cottage Manager"
        );
        return fineRecordRepository.save(fine);
    }

    public FineRecord payFine(Long fineId) {
        FineRecord fine = fineRecordRepository.findById(fineId)
                .orElseThrow(() -> new IllegalArgumentException("Fine record not found with ID: " + fineId));
        fine.setPaymentStatus("PAID");
        fine.setPaidAt(LocalDateTime.now());
        return fineRecordRepository.save(fine);
    }

    @Transactional(readOnly = true)
    public List<FineRecord> getFinesByReservation(Long reservationId) {
        return fineRecordRepository.findByReservationId(reservationId);
    }

    @Transactional(readOnly = true)
    public List<GuestLog> getGuestLogs(Long reservationId) {
        if (reservationId != null) {
            return guestLogRepository.findByReservationId(reservationId);
        }
        return guestLogRepository.findAllByOrderByCreatedAtDesc();
    }

    // ==========================================
    // 4. COTTAGE OCCUPANCY & USAGE REPORTS
    // ==========================================

    @Transactional(readOnly = true)
    public OccupancyReportDTO generateOccupancyReport(LocalDate startDate, LocalDate endDate) {
        LocalDate start = (startDate != null) ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate end = (endDate != null) ? endDate : LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1);

        List<Cottage> allCottages = cottageRepository.findAll();
        List<CottageReservation> reservations = reservationRepository.findReservationsInDateRange(start, end);

        OccupancyReportDTO report = new OccupancyReportDTO();
        report.setReportStartDate(start);
        report.setReportEndDate(end);
        report.setTotalCottages(allCottages.size());
        report.setTotalReservations(reservations.size());

        long pending = 0;
        long active = 0;
        long completed = 0;
        long cancelled = 0;
        BigDecimal accRevenue = BigDecimal.ZERO;
        BigDecimal extraBedRevenue = BigDecimal.ZERO;
        Map<String, Long> byType = new HashMap<>();
        Map<String, Long> statusDist = new HashMap<>();

        long totalBookedNights = 0;

        for (CottageReservation r : reservations) {
            String status = r.getStatus();
            statusDist.put(status, statusDist.getOrDefault(status, 0L) + 1);

            String cType = r.getCottage().getCottageType();
            byType.put(cType, byType.getOrDefault(cType, 0L) + 1);

            if ("PENDING".equalsIgnoreCase(status)) {
                pending++;
            } else if ("CHECKED_IN".equalsIgnoreCase(status)) {
                active++;
            } else if ("CHECKED_OUT".equalsIgnoreCase(status)) {
                completed++;
            } else if ("CANCELLED".equalsIgnoreCase(status) || "REJECTED".equalsIgnoreCase(status)) {
                cancelled++;
            }

            if (!"CANCELLED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
                accRevenue = accRevenue.add(r.getBaseAmount());
                extraBedRevenue = extraBedRevenue.add(r.getExtraBedFee());
                long stayNights = ChronoUnit.DAYS.between(r.getCheckInDate(), r.getCheckOutDate());
                totalBookedNights += Math.max(1, stayNights);
            }
        }

        long daysInRange = Math.max(1, ChronoUnit.DAYS.between(start, end) + 1);
        long totalAvailableRoomNights = Math.max(1, allCottages.size() * daysInRange);
        double occupancyRate = ((double) totalBookedNights / (double) totalAvailableRoomNights) * 100.0;
        if (occupancyRate > 100.0) occupancyRate = 100.0;

        BigDecimal finesPaid = fineRecordRepository.sumTotalPaidFines();
        if (finesPaid == null) finesPaid = BigDecimal.ZERO;

        report.setPendingApprovals(pending);
        report.setActiveCheckedIn(active);
        report.setCompletedStays(completed);
        report.setCancelledReservations(cancelled);
        report.setOccupancyRatePercent(Math.round(occupancyRate * 10.0) / 10.0);
        report.setTotalAccommodationRevenue(accRevenue.setScale(2, RoundingMode.HALF_UP));
        report.setTotalExtraBedRevenue(extraBedRevenue.setScale(2, RoundingMode.HALF_UP));
        report.setTotalFinesCollected(finesPaid.setScale(2, RoundingMode.HALF_UP));
        report.setGrossTotalRevenue(accRevenue.add(extraBedRevenue).add(finesPaid).setScale(2, RoundingMode.HALF_UP));
        report.setReservationsByType(byType);
        report.setStatusDistribution(statusDist);

        return report;
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    private void validateDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null) {
            throw new IllegalArgumentException("Check-in date cannot be null");
        }
        if (checkOut == null) {
            throw new IllegalArgumentException("Check-out date cannot be null");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be at least one day after check-in date");
        }
        long duration = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (duration > 30) {
            throw new IllegalArgumentException("Maximum reservation duration is 30 nights");
        }
    }

    private String generateReservationCode(LocalDate checkIn) {
        String datePart = checkIn.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "COT-" + datePart + "-" + randomPart;
    }

    private String HOURLY_RATE_STRING() {
        return HOURLY_OVERSTAY_RATE.toPlainString();
    }
}