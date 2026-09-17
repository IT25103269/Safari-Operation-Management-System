package com.safari.app.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "vehicle_maintenance_issues")
public class VehicleMaintenanceIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "issue_title", nullable = false, length = 120)
    private String issueTitle;

    @Column(name = "description", nullable = false, length = 700)
    private String description;

    @Column(name = "reported_by", length = 100)
    private String reportedBy;

    @Column(name = "priority", length = 30)
    private String priority = "Medium"; // High, Medium, Low

    @Column(name = "status", nullable = false, length = 30)
    private String status = "OPEN"; // OPEN, IN_PROGRESS, RESOLVED

    @Column(name = "reported_at", nullable = false)
    private Timestamp reportedAt;

    public VehicleMaintenanceIssue() {
        this.reportedAt = new Timestamp(System.currentTimeMillis());
    }

    public VehicleMaintenanceIssue(Long vehicleId, String issueTitle, String description,
                                   String reportedBy, String priority, String status) {
        this.vehicleId = vehicleId;
        this.issueTitle = issueTitle;
        this.description = description;
        this.reportedBy = reportedBy;
        this.priority = priority != null ? priority : "Medium";
        this.status = status != null ? status : "OPEN";
        this.reportedAt = new Timestamp(System.currentTimeMillis());
    }

    @PrePersist
    protected void onCreate() {
        if (this.reportedAt == null) {
            this.reportedAt = new Timestamp(System.currentTimeMillis());
        }
        if (this.status == null) {
            this.status = "OPEN";
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getIssueTitle() { return issueTitle; }
    public void setIssueTitle(String issueTitle) { this.issueTitle = issueTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getReportedAt() { return reportedAt; }
    public void setReportedAt(Timestamp reportedAt) { this.reportedAt = reportedAt; }
}
