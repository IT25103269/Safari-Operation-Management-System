--create database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'LankaWildTrailsDB')
BEGIN
    CREATE DATABASE LankaWildTrailsDB;
END
GO

USE LankaWildTrailsDB;
GO

--table01 (Users)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Users')
BEGIN
CREATE TABLE Users (
                       UserID INT IDENTITY(1,1) PRIMARY KEY,
                       FullName VARCHAR(100) NOT NULL,
                       Email VARCHAR(100) UNIQUE NOT NULL,
                       PasswordHash VARCHAR(255) NOT NULL,
                       Role VARCHAR(30) CHECK (Role IN ('Tourist', 'admin', 'guide','manager','driver')) NOT NULL
);
END
GO

--table02 (safari-package)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'SafariPackages')
BEGIN
CREATE TABLE SafariPackages (
                                PackageID INT IDENTITY(1,1) PRIMARY KEY,
                                PackageName VARCHAR(150) NOT NULL,
                                Destination VARCHAR(100) NOT NULL,
                                DurationDays INT NOT NULL CHECK (DurationDays > 0),
                                PricePerPerson DECIMAL(10,2) NOT NULL CHECK (PricePerPerson >= 0.00),
                                MaxGroupSize INT NOT NULL CHECK (MaxGroupSize > 0),
                                ItinerarySummary VARCHAR(MAX) NOT NULL,
    IncludedServices VARCHAR(MAX) NULL,
    Status VARCHAR(20) DEFAULT 'Active'
        CHECK (Status IN ('Active', 'Archived')),
    CreatedAt DATETIME DEFAULT GETDATE(),
    UpdatedAt DATETIME DEFAULT GETDATE(),
    INDEX idx_destination (Destination),
    INDEX idx_price (PricePerPerson),
    INDEX idx_status (Status)
);
END

--table03(vehicles)
USE LankaWildTrailsDB;
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'vehicles')
BEGIN
CREATE TABLE vehicles (
                          id BIGINT IDENTITY(1,1) PRIMARY KEY,
                          registration_number VARCHAR(30) NOT NULL UNIQUE,
                          vehicle_type VARCHAR(50) NOT NULL,
                          seating_capacity INT NOT NULL CHECK (seating_capacity > 0),
                          assigned_driver VARCHAR(100) NULL,
                          status VARCHAR(30) NOT NULL
                              CHECK (status IN ('AVAILABLE', 'IN_USE', 'UNDER_MAINTENANCE', 'UNAVAILABLE')),
                          description VARCHAR(500) NULL
);
END
GO

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'vehicle_maintenance_issues')
BEGIN
CREATE TABLE vehicle_maintenance_issues (
                                            id BIGINT IDENTITY(1,1) PRIMARY KEY,
                                            vehicle_id BIGINT NOT NULL,
                                            issue_title VARCHAR(120) NOT NULL,
                                            description VARCHAR(700) NOT NULL,
                                            reported_by VARCHAR(100) NULL,
                                            priority VARCHAR(30) NULL,
                                            status VARCHAR(30) NOT NULL
                                                CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED')),
                                            reported_at DATETIME NOT NULL DEFAULT GETDATE(),

                                            CONSTRAINT fk_maintenance_issue_vehicle
                                                FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);
END
GO
--table04(TripAllocations)

--table05(CottageReservations)

--table06(WildlifeSightings)

--table07(WildlifeSpecies)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'WildlifeSpecies')
BEGIN
CREATE TABLE WildlifeSpecies (
                                 SpeciesID INT IDENTITY(1,1) PRIMARY KEY,
                                 CommonName VARCHAR(100) NOT NULL,
                                 ScientificName VARCHAR(150) NOT NULL,
                                 Category VARCHAR(50) NOT NULL CHECK (Category IN ('Mammal', 'Bird', 'Reptile', 'Amphibian', 'Other')),
                                 ConservationStatus VARCHAR(50) NOT NULL CHECK (ConservationStatus IN ('Critically Endangered', 'Endangered', 'Vulnerable', 'Near Threatened', 'Least Concern')),
                                 Habitat VARCHAR(255) NOT NULL,
                                 PrimaryPark VARCHAR(100) NOT NULL, -- e.g., Yala, Wilpattu, Udawalawe, Minneriya, Bundala
                                 DietaryHabit VARCHAR(50) CHECK (DietaryHabit IN ('Carnivore', 'Herbivore', 'Omnivore', 'Piscivore')),
                                 Description VARCHAR(MAX) NOT NULL,
    BestSpottingTime VARCHAR(150) NULL,
    ImageURL VARCHAR(500) NULL,
    Status VARCHAR(20) DEFAULT 'Active' CHECK (Status IN ('Active', 'Archived')),
    CreatedAt DATETIME DEFAULT GETDATE(),
    UpdatedAt DATETIME DEFAULT GETDATE(),

    INDEX idx_species_name (CommonName),
    INDEX idx_species_category (Category),
    INDEX idx_species_park (PrimaryPark),
    INDEX idx_species_status (Status)
);

END
GO