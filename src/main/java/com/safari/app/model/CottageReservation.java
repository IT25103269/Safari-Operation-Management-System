package com.safari.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "CottageReservations")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CottageReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReservationID")
    private Integer id;

    @Column(name = "ReservationCode", length = 50)
    private String reservationCode;

    @Column(name = "CottageID", insertable = false, updatable = false)
    private Integer cottageId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CottageID", nullable = false)
    private Cottage cottage;

    @Column(name = "TouristName", nullable = false, length = 100)
    private String touristName;

    @Column(name = "TouristEmail", nullable = false, length = 100)
    private String touristEmail;

    @Column(name = "TouristPhone", length = 30)
    private String touristPhone;

    @Column(name = "CheckInDate", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "CheckOutDate", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "GuestsCount", nullable = false)
    private Integer guestsCount = 1;

    @Column(name = "ExtraBeds")
    private Integer extraBeds = 0;

    @Column(name = "TotalPrice", nullable = false)
    private Double totalPrice = 0.0;

    @Column(name = "SpecialRequests", length = 500)
    private String specialRequests;

    @Column(name = "Status", nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, REJECTED, CANCELLED

    @Column(name = "CreatedAt", updatable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    public CottageReservation() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public Integer getCottageId() {
        return cottageId;
    }

    public void setCottageId(Integer cottageId) {
        this.cottageId = cottageId;
    }

    public Cottage getCottage() {
        return cottage;
    }

    public void setCottage(Cottage cottage) {
        this.cottage = cottage;
    }

    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    // Alias for frontend
    public String getGuestName() {
        return touristName;
    }

    public void setGuestName(String guestName) {
        this.touristName = guestName;
    }

    public String getTouristEmail() {
        return touristEmail;
    }

    public void setTouristEmail(String touristEmail) {
        this.touristEmail = touristEmail;
    }

    // Alias for frontend
    public String getGuestEmail() {
        return touristEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.touristEmail = guestEmail;
    }

    public String getTouristPhone() {
        return touristPhone;
    }

    public void setTouristPhone(String touristPhone) {
        this.touristPhone = touristPhone;
    }

    // Alias for frontend
    public String getGuestPhone() {
        return touristPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.touristPhone = guestPhone;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getGuestsCount() {
        return guestsCount;
    }

    public void setGuestsCount(Integer guestsCount) {
        this.guestsCount = guestsCount;
    }

    // Alias for frontend
    public Integer getNumberOfGuests() {
        return guestsCount;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.guestsCount = numberOfGuests;
    }

    public Integer getExtraBeds() {
        return extraBeds;
    }

    public void setExtraBeds(Integer extraBeds) {
        this.extraBeds = extraBeds;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Alias for frontend
    public Double getTotalAmount() {
        return totalPrice;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalPrice = totalAmount;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
