package com.safari.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Guest review linked to the authenticated user and the cottage being reviewed.
 * Reviews are moderated before they are shown on the public website.
 */
@Entity
@Table(name = "reviews", indexes = {
        @Index(name = "ix_review_cottage_status", columnList = "cottage_id,status"),
        @Index(name = "ix_review_user", columnList = "user_id")
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cottage_id", nullable = false)
    @JsonIgnore
    private Cottage cottage;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    private String comment;

    @Size(max = 50)
    @Column(name = "booking_type", length = 50)
    private String bookingType;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Review() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null || status.isBlank()) status = "PENDING";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @JsonIgnore
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @JsonIgnore
    public Cottage getCottage() { return cottage; }
    public void setCottage(Cottage cottage) { this.cottage = cottage; }

    public Long getUserId() { return user != null ? user.getId() : null; }
    public Long getCottageId() { return cottage != null ? cottage.getId() : null; }
    public String getUserName() { return user != null ? user.getFullName() : "Guest"; }
    public String getUserEmail() { return user != null ? user.getEmail() : ""; }
    public String getCottageName() { return cottage != null ? cottage.getCottageName() : "Cottage"; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getBookingType() { return bookingType; }
    public void setBookingType(String bookingType) { this.bookingType = bookingType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Frontend compatibility aliases.
    public String getText() { return comment; }
    public String getName() { return getUserName(); }
    public String getBooked() { return bookingType; }
    public String getDate() { return createdAt != null ? createdAt.toLocalDate().toString() : ""; }
}
