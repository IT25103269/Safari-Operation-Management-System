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
                       Role VARCHAR(30) CHECK (Role IN ('Tourist', 'admin', 'guide','manager')) NOT NULL
);
END
GO

--table02 (safari-package)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'SafariPackages')
BEGIN
CREATE TABLE SafariPackages (
                                PackageID INT IDENTITY(1,1) PRIMARY KEY,
                                PackageName VARCHAR(150) NOT NULL,
                                Destination VARCHAR(100) NOT NULL,              -- e.g., Yala, Wilpattu, Udawalawe
                                DurationDays INT NOT NULL CHECK (DurationDays > 0),
                                PricePerPerson DECIMAL(10,2) NOT NULL CHECK (PricePerPerson >= 0.00),
                                MaxGroupSize INT NOT NULL CHECK (MaxGroupSize > 0),
                                ItinerarySummary VARCHAR(MAX) NOT NULL,
    IncludedServices VARCHAR(MAX) NULL,
    Status VARCHAR(20) DEFAULT 'Active'
        CHECK (Status IN ('Active', 'Archived')),   -- Controls catalogue status
    CreatedAt DATETIME DEFAULT GETDATE(),
    UpdatedAt DATETIME DEFAULT GETDATE(),

    -- Performance Indexes for tourist search and filtering
    INDEX idx_destination (Destination),
    INDEX idx_price (PricePerPerson),
    INDEX idx_status (Status)
);
END

--table03(vehicles)

--table04(TripAllocations)

--table05(CottageReservations)

--table06(WildlifeSightings)

--table07(WildlifeSpecies)