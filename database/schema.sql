-- Lanka Wild Trails (Pvt) Ltd - Database Schema for Microsoft SQL Server
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'LankaWildTrailsDB')
BEGIN
    CREATE DATABASE LankaWildTrailsDB;
END
GO

USE LankaWildTrailsDB;
GO

-- Table 01: Users
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Users')
BEGIN
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    FullName VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    Role VARCHAR(30) NOT NULL
);
END
GO

-- Table 02: SafariPackages
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
    Status VARCHAR(20) DEFAULT 'Active' CHECK (Status IN ('Active', 'Archived')),
    CreatedAt DATETIME DEFAULT GETDATE(),
    UpdatedAt DATETIME DEFAULT GETDATE()
);
END
GO

-- Table 03: Vehicles
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'vehicles')
BEGIN
CREATE TABLE vehicles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    registration_number VARCHAR(30) NOT NULL UNIQUE,
    vehicle_type VARCHAR(50) NOT NULL,
    seating_capacity INT NOT NULL CHECK (seating_capacity > 0),
    assigned_driver VARCHAR(100) NULL,
    status VARCHAR(30) NOT NULL CHECK (status IN ('AVAILABLE', 'IN_USE', 'UNDER_MAINTENANCE', 'UNAVAILABLE')),
    description VARCHAR(500) NULL
);
END
GO

-- Table 04: Vehicle Maintenance Issues
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'vehicle_maintenance_issues')
BEGIN
CREATE TABLE vehicle_maintenance_issues (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    issue_title VARCHAR(120) NOT NULL,
    description VARCHAR(700) NOT NULL,
    reported_by VARCHAR(100) NULL,
    priority VARCHAR(30) NULL,
    status VARCHAR(30) NOT NULL CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED')),
    reported_at DATETIME NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_maintenance_issue_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);
END
GO

-- Table 05: SafariBookings
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Bookings')
BEGIN
CREATE TABLE Bookings (
    BookingID INT IDENTITY(1,1) PRIMARY KEY,
    BookingRef VARCHAR(50) NOT NULL UNIQUE,
    TouristID INT NULL,
    TouristName VARCHAR(100) NOT NULL,
    TouristEmail VARCHAR(100) NOT NULL,
    TouristPhone VARCHAR(30) NULL,
    PackageID INT NULL,
    PackageName VARCHAR(150) NOT NULL,
    Destination VARCHAR(100) NOT NULL,
    TravelDate DATE NOT NULL,
    ParticipantsCount INT NOT NULL CHECK (ParticipantsCount > 0),
    TotalPrice DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    SpecialRequests VARCHAR(500) NULL,
    Status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (Status IN ('PENDING', 'CONFIRMED', 'ALLOCATED', 'COMPLETED', 'CANCELLED')),
    AssignedGuide VARCHAR(100) NULL,
    AssignedVehicle VARCHAR(100) NULL,
    CreatedAt DATETIME DEFAULT GETDATE()
);
END
GO

-- Table 06: WildlifeSpecies
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'WildlifeSpecies')
BEGIN
CREATE TABLE WildlifeSpecies (
    SpeciesID INT IDENTITY(1,1) PRIMARY KEY,
    CommonName VARCHAR(100) NOT NULL,
    ScientificName VARCHAR(150) NOT NULL,
    Category VARCHAR(50) NOT NULL CHECK (Category IN ('Mammal', 'Bird', 'Reptile', 'Amphibian', 'Other')),
    ConservationStatus VARCHAR(50) NOT NULL CHECK (ConservationStatus IN ('Critically Endangered', 'Endangered', 'Vulnerable', 'Near Threatened', 'Least Concern')),
    Habitat VARCHAR(255) NOT NULL,
    PrimaryPark VARCHAR(100) NOT NULL,
    DietaryHabit VARCHAR(50) NULL,
    Description VARCHAR(MAX) NOT NULL,
    BestSpottingTime VARCHAR(150) NULL,
    ImageURL VARCHAR(500) NULL,
    Status VARCHAR(20) DEFAULT 'Active' CHECK (Status IN ('Active', 'Archived')),
    CreatedAt DATETIME DEFAULT GETDATE(),
    UpdatedAt DATETIME DEFAULT GETDATE()
);
END
GO

-- Table 07: WildlifeSightings
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'WildlifeSightings')
BEGIN
CREATE TABLE WildlifeSightings (
    SightingID INT IDENTITY(1,1) PRIMARY KEY,
    TripRef VARCHAR(50) NULL,
    SpeciesName VARCHAR(150) NOT NULL,
    SightingDate DATE NOT NULL,
    SightingTime VARCHAR(30) NOT NULL,
    ParkLocation VARCHAR(100) NOT NULL,
    SpecificLocation VARCHAR(255) NOT NULL,
    PhotoURL VARCHAR(500) NULL,
    Notes VARCHAR(1000) NULL,
    LoggedBy VARCHAR(100) NOT NULL,
    Status VARCHAR(30) NOT NULL DEFAULT 'Pending' CHECK (Status IN ('Pending', 'Verified', 'Rejected', 'Unverified')),
    CreatedAt DATETIME DEFAULT GETDATE()
);
END
GO

-- Table 08: Cottages
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Cottages')
BEGIN
CREATE TABLE Cottages (
    CottageID INT IDENTITY(1,1) PRIMARY KEY,
    CottageNumber VARCHAR(30) NULL,
    CottageName VARCHAR(100) NOT NULL,
    Park VARCHAR(100) NOT NULL,
    RoomType VARCHAR(50) NOT NULL,
    Capacity INT NOT NULL CHECK (Capacity > 0),
    PricePerNight DECIMAL(10,2) NOT NULL CHECK (PricePerNight >= 0.00),
    Status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE' CHECK (Status IN ('AVAILABLE', 'BOOKED', 'OCCUPIED', 'MAINTENANCE')),
    Amenities VARCHAR(500) NULL,
    ImageURL VARCHAR(500) NULL,
    Description VARCHAR(MAX) NULL
);
END
GO

-- Table 09: CottageReservations
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'CottageReservations')
BEGIN
CREATE TABLE CottageReservations (
    ReservationID INT IDENTITY(1,1) PRIMARY KEY,
    ReservationCode VARCHAR(50) NULL,
    CottageID INT NOT NULL,
    TouristName VARCHAR(100) NOT NULL,
    TouristEmail VARCHAR(100) NOT NULL,
    TouristPhone VARCHAR(30) NULL,
    CheckInDate DATE NOT NULL,
    CheckOutDate DATE NOT NULL,
    GuestsCount INT NOT NULL CHECK (GuestsCount > 0),
    ExtraBeds INT DEFAULT 0,
    TotalPrice DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    SpecialRequests VARCHAR(500) NULL,
    Status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (Status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'REJECTED', 'CANCELLED')),
    CreatedAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_reservation_cottage FOREIGN KEY (CottageID) REFERENCES Cottages(CottageID)
);
END
GO