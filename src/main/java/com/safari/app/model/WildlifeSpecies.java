package com.safari.app.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "WildlifeSpecies")
public class WildlifeSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SpeciesID")
    private Integer speciesId;

    @Column(name = "CommonName", nullable = false, length = 100)
    private String commonName;

    @Column(name = "ScientificName", nullable = false, length = 150)
    private String scientificName;

    @Column(name = "Category", nullable = false, length = 50)
    private String category; // Mammal, Bird, Reptile, Amphibian, Other

    @Column(name = "ConservationStatus", nullable = false, length = 50)
    private String conservationStatus; // Critically Endangered, Endangered, Vulnerable, Near Threatened, Least Concern

    @Column(name = "Habitat", length = 255)
    private String habitat;

    @Column(name = "PrimaryPark", nullable = false, length = 100)
    private String primaryPark; // Yala, Wilpattu, Udawalawe, Minneriya, Bundala, Sinharaja, Horton Plains

    @Column(name = "DietaryHabit", length = 50)
    private String dietaryHabit; // Carnivore, Herbivore, Omnivore, Piscivore

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "BestSpottingTime", length = 150)
    private String bestSpottingTime;

    @Column(name = "ImageURL", length = 500)
    private String imageUrl;

    @Column(name = "Status", length = 20)
    private String status = "Active";

    @Column(name = "CreatedAt")
    private Timestamp createdAt;

    @Column(name = "UpdatedAt")
    private Timestamp updatedAt;

    public WildlifeSpecies() {
        this.status = "Active";
    }

    public WildlifeSpecies(String commonName, String scientificName, String category,
                           String conservationStatus, String habitat, String primaryPark,
                           String dietaryHabit, String description, String bestSpottingTime,
                           String imageUrl) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.category = category;
        this.conservationStatus = conservationStatus;
        this.habitat = habitat;
        this.primaryPark = primaryPark;
        this.dietaryHabit = dietaryHabit;
        this.description = description;
        this.bestSpottingTime = bestSpottingTime;
        this.imageUrl = imageUrl;
        this.status = "Active";
    }

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = "Active";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public Integer getSpeciesId() { return speciesId; }
    public void setSpeciesId(Integer speciesId) { this.speciesId = speciesId; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getConservationStatus() { return conservationStatus; }
    public void setConservationStatus(String conservationStatus) { this.conservationStatus = conservationStatus; }

    public String getHabitat() { return habitat; }
    public void setHabitat(String habitat) { this.habitat = habitat; }

    public String getPrimaryPark() { return primaryPark; }
    public void setPrimaryPark(String primaryPark) { this.primaryPark = primaryPark; }

    public String getDietaryHabit() { return dietaryHabit; }
    public void setDietaryHabit(String dietaryHabit) { this.dietaryHabit = dietaryHabit; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBestSpottingTime() { return bestSpottingTime; }
    public void setBestSpottingTime(String bestSpottingTime) { this.bestSpottingTime = bestSpottingTime; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
