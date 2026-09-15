package com.safari.app.cottage_mgmt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class OccupancyReportDTO {
    private LocalDate reportStartDate;
    private LocalDate reportEndDate;
    private long totalCottages;
    private long totalReservations;
    private long pendingApprovals;
    private long activeCheckedIn;
    private long completedStays;
    private long cancelledReservations;
    private double occupancyRatePercent;
    private BigDecimal totalAccommodationRevenue;
    private BigDecimal totalExtraBedRevenue;
    private BigDecimal totalFinesCollected;
    private BigDecimal grossTotalRevenue;
    private Map<String, Long> reservationsByType;
    private Map<String, Long> statusDistribution;

    public OccupancyReportDTO() {}

    public LocalDate getReportStartDate() { return reportStartDate; }
    public void setReportStartDate(LocalDate reportStartDate) { this.reportStartDate = reportStartDate; }

    public LocalDate getReportEndDate() { return reportEndDate; }
    public void setReportEndDate(LocalDate reportEndDate) { this.reportEndDate = reportEndDate; }

    public long getTotalCottages() { return totalCottages; }
    public void setTotalCottages(long totalCottages) { this.totalCottages = totalCottages; }

    public long getTotalReservations() { return totalReservations; }
    public void setTotalReservations(long totalReservations) { this.totalReservations = totalReservations; }

    public long getPendingApprovals() { return pendingApprovals; }
    public void setPendingApprovals(long pendingApprovals) { this.pendingApprovals = pendingApprovals; }

    public long getActiveCheckedIn() { return activeCheckedIn; }
    public void setActiveCheckedIn(long activeCheckedIn) { this.activeCheckedIn = activeCheckedIn; }

    public long getCompletedStays() { return completedStays; }
    public void setCompletedStays(long completedStays) { this.completedStays = completedStays; }

    public long getCancelledReservations() { return cancelledReservations; }
    public void setCancelledReservations(long cancelledReservations) { this.cancelledReservations = cancelledReservations; }

    public double getOccupancyRatePercent() { return occupancyRatePercent; }
    public void setOccupancyRatePercent(double occupancyRatePercent) { this.occupancyRatePercent = occupancyRatePercent; }

    public BigDecimal getTotalAccommodationRevenue() { return totalAccommodationRevenue; }
    public void setTotalAccommodationRevenue(BigDecimal totalAccommodationRevenue) { this.totalAccommodationRevenue = totalAccommodationRevenue; }

    public BigDecimal getTotalExtraBedRevenue() { return totalExtraBedRevenue; }
    public void setTotalExtraBedRevenue(BigDecimal totalExtraBedRevenue) { this.totalExtraBedRevenue = totalExtraBedRevenue; }

    public BigDecimal getTotalFinesCollected() { return totalFinesCollected; }
    public void setTotalFinesCollected(BigDecimal totalFinesCollected) { this.totalFinesCollected = totalFinesCollected; }

    public BigDecimal getGrossTotalRevenue() { return grossTotalRevenue; }
    public void setGrossTotalRevenue(BigDecimal grossTotalRevenue) { this.grossTotalRevenue = grossTotalRevenue; }

    public Map<String, Long> getReservationsByType() { return reservationsByType; }
    public void setReservationsByType(Map<String, Long> reservationsByType) { this.reservationsByType = reservationsByType; }

    public Map<String, Long> getStatusDistribution() { return statusDistribution; }
    public void setStatusDistribution(Map<String, Long> statusDistribution) { this.statusDistribution = statusDistribution; }
}