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

--table03(vehicles)

--table04(TripAllocations)

--table05(CottageReservations)

--table06(WildlifeSightings)

--table07(WildlifeSpecies)