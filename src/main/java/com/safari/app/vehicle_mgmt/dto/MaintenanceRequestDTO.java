package com.safari.app.vehicle_mgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MaintenanceRequestDTO {

    @NotBlank(message = "Issue description is required")
    @Size(min = 3, max = 1000, message = "Issue description must be between 3 and 1000 characters")
    private String issueDescription;

    @Pattern(regexp = "LOW|MEDIUM|HIGH|CRITICAL", message = "Severity must be LOW, MEDIUM, HIGH or CRITICAL")
    private String severity = "MEDIUM"; // LOW, MEDIUM, HIGH, CRITICAL
    @Size(max = 100, message = "Reported by cannot exceed 100 characters")
    private String reportedBy;

    public MaintenanceRequestDTO() {}

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
}