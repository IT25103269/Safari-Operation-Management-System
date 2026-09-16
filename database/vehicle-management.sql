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
