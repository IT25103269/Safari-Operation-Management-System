package com.safari.app.cottage_mgmt.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FineRequestDTO {
    @NotBlank(message = "Fine type is required")
    private String fineType; // OVERSTAY, ROOM_DAMAGE, LATE_CHECKOUT, OTHER

    @NotNull(message = "Fine amount is required")
    @DecimalMin(value = "0.01", message = "Fine amount must be greater than zero")
    private BigDecimal fineAmount;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String assessedBy;

    public FineRequestDTO() {}

    public String getFineType() { return fineType; }
    public void setFineType(String fineType) { this.fineType = fineType; }

    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getAssessedBy() { return assessedBy; }
    public void setAssessedBy(String assessedBy) { this.assessedBy = assessedBy; }
}