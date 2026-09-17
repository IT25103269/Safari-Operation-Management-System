package com.safari.app.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "Bookings")
public class SafariBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID")
    private Integer bookingId;

    @Column(name = "BookingRef", nullable = false, unique = true, length = 50)
    private String bookingRef;

    @Column(name = "TouristID")
    private Integer touristId;

    @Column(name = "TouristName", nullable = false, length = 100)
    private String touristName;

    @Column(name = "TouristEmail", nullable = false, length = 100)
    private String touristEmail;

    @Column(name = "TouristPhone", length = 30)
    private String touristPhone;

    @Column(name = "PackageID")
    private Integer packageId;

    @Column(name = "PackageName", nullable = false, length = 150)
    private String packageName;

    @Column(name = "Destination", nullable = false, length = 100)
    private String destination;

    @Column(name = "TravelDate", nullable = false)
    private LocalDate travelDate;

    @Column(name = "ParticipantsCount", nullable = false)
    private Integer participantsCount;

    @Column(name = "TotalPrice", nullable = false)
    private Double totalPrice = 0.0;

    @Column(name = "SpecialRequests", length = 500)
    private String specialRequests;

    @Column(name = "Status", nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, CONFIRMED, ALLOCATED, COMPLETED, CANCELLED

    @Column(name = "AssignedGuide", length = 100)
    private String assignedGuide;

    @Column(name = "AssignedVehicle", length = 100)
    private String assignedVehicle;

    @Column(name = "CreatedAt")
    private Timestamp createdAt;

    public SafariBooking() {
        this.status = "PENDING";
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Timestamp(System.currentTimeMillis());
        }
        if (this.status == null) {
            this.status = "PENDING";
        }
    }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public String getBookingRef() { return bookingRef; }
    public void setBookingRef(String bookingRef) { this.bookingRef = bookingRef; }

    public Integer getTouristId() { return touristId; }
    public void setTouristId(Integer touristId) { this.touristId = touristId; }

    public String getTouristName() { return touristName; }
    public void setTouristName(String touristName) { this.touristName = touristName; }

    public String getTouristEmail() { return touristEmail; }
    public void setTouristEmail(String touristEmail) { this.touristEmail = touristEmail; }

    public String getTouristPhone() { return touristPhone; }
    public void setTouristPhone(String touristPhone) { this.touristPhone = touristPhone; }

    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }

    public Integer getParticipantsCount() { return participantsCount; }
    public void setParticipantsCount(Integer participantsCount) { this.participantsCount = participantsCount; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedGuide() { return assignedGuide; }
    public void setAssignedGuide(String assignedGuide) { this.assignedGuide = assignedGuide; }

    public String getAssignedVehicle() { return assignedVehicle; }
    public void setAssignedVehicle(String assignedVehicle) { this.assignedVehicle = assignedVehicle; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
