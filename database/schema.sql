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

-- Insert Initial Seed Data for WildlifeSpecies (Idempotent)
IF NOT EXISTS (SELECT 1 FROM WildlifeSpecies)
BEGIN
INSERT INTO WildlifeSpecies (CommonName, ScientificName, Category, ConservationStatus, Habitat, PrimaryPark, DietaryHabit, Description, BestSpottingTime, ImageURL, Status)
VALUES
('Sri Lankan Leopard', 'Panthera pardus kotiya', 'Mammal', 'Endangered', 'Dry zone scrub forest, arid scrubland, and rocky hills', 'Yala', 'Carnivore', 'Endemic apex predator of Sri Lanka. Yala National Park (Block 1) possesses one of the densest wild leopard populations in the world.', 'Early morning (06:00 - 08:30) and late afternoon around waterholes', 'https://images.unsplash.com/photo-1456926631375-92c8ce872def?w=600', 'Active'),
('Asian Elephant', 'Elephas maximus maximus', 'Mammal', 'Endangered', 'Grasslands, scrub jungles, riverine woodlands, and reservoir banks', 'Udawalawe', 'Herbivore', 'The Sri Lankan elephant is the largest and darkest subspecies of the Asian elephant. Udawalawe and Minneriya offer world-renowned gathering sightings.', 'Late afternoon (15:00 - 18:00) during reservoir baths', 'https://images.unsplash.com/photo-1557050543-4d5f4e07ef46?w=600', 'Active'),
('Sri Lankan Sloth Bear', 'Melursus ursinus inornatus', 'Mammal', 'Vulnerable', 'Dense lowland dry zone forests with rock outcrops and termite hills', 'Wilpattu', 'Omnivore', 'Highly elusive and solitary bear subspecies with a shaggy coat. Feeds heavily on termites, honeycomb, and sweet Palu and Weera tree berries.', 'Early dawn (05:45 - 07:15) during dry season fruiting', 'https://images.unsplash.com/photo-1589656966895-2f33e7653819?w=600', 'Active'),
('Mugger Crocodile', 'Crocodylus palustris', 'Reptile', 'Vulnerable', 'Freshwater lakes, natural villus, slow rivers, and reservoirs', 'Bundala', 'Carnivore', 'Freshwater broad-snouted crocodile often seen sunbathing motionless along the sunny banks of villus in lowland dry zones.', 'Mid-day sun basking (10:00 - 14:00)', 'https://images.unsplash.com/photo-1522069169874-c58ec4b76be5?w=600', 'Active'),
('Sri Lanka Blue Magpie', 'Urocissa ornata', 'Bird', 'Vulnerable', 'Wet zone hill rainforest canopies and dense foothill jungles', 'Sinharaja', 'Omnivore', 'Vibrant endemic bird with a fiery coral-red bill and legs, deep chestnut head and wings, and bright cobalt blue plumage. Moves in lively family flocks.', 'Morning canopy feeding tours (07:00 - 10:00)', 'https://images.unsplash.com/photo-1552728089-57bdde30beb3?w=600', 'Active'),
('Sri Lanka Junglefowl', 'Gallus lafayettii', 'Bird', 'Least Concern', 'Lowland rainforests, dry forest floor, and scrub boundaries', 'Sinharaja', 'Omnivore', 'The National Bird of Sri Lanka. Endemic galliform known for the male''s vivid orange-red comb with a yellow centre and iridescent body plumage.', 'Early morning forest margins (06:30 - 08:30)', 'https://images.unsplash.com/photo-1548767797-d8c844163c4c?w=600', 'Active'),
('Sambar Deer', 'Rusa unicolor unicolor', 'Mammal', 'Vulnerable', 'Highland plateau cloud forests, montane grasslands, and dry forest fringes', 'Horton Plains', 'Herbivore', 'Sri Lanka''s largest deer species. Large stags carry prominent three-tined antlers and browse across misty highland plains.', 'Early morning mist (06:00 - 08:00) and twilight', 'https://images.unsplash.com/photo-1484406566174-9da000fda645?w=600', 'Active'),
('Sri Lankan Spotted Deer', 'Axis axis ceylonensis', 'Mammal', 'Least Concern', 'Open savannahs, forest clearing fringes, and waterhole banks', 'Yala', 'Herbivore', 'Graceful herbivore living in large herds. Recognized by its reddish-fawn coat dotted with white spots; vital prey species for leopards.', 'Throughout safari game drives, especially dawn and dusk', 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=600', 'Active'),
('Spot-billed Pelican', 'Pelecanus philippensis', 'Bird', 'Near Threatened', 'Coastal wetlands, saline lagoons, and shallow inland lakes', 'Bundala', 'Piscivore', 'Large waterbird with a distinctive pouch and speckled beak. Breeds in colonies near coastal lagoons in Bundala, a RAMSAR wetland.', 'Morning feeding sessions (07:30 - 11:00)', 'https://images.unsplash.com/photo-1444464666168-49d633b86797?w=600', 'Active'),
('Purple-faced Langur', 'Semnopithecus vetulus', 'Mammal', 'Endangered', 'High canopy rainforests, wet zone foliage, and riverine forests', 'Sinharaja', 'Herbivore', 'Endemic arboreal leaf-monkey recognized by its dark body, prominent white side-whiskers, and characteristic territorial loud calls.', 'Active throughout mid-morning in upper tree canopy', 'https://images.unsplash.com/photo-1540573133985-87b6da6d54a9?w=600', 'Active'),
('Crested Serpent Eagle', 'Spilornis cheela', 'Bird', 'Least Concern', 'Forested savannas, dry woodlands, and wetland edges', 'Yala', 'Carnivore', 'Powerful raptor with a bare yellow face and crest. Often spotted perched upright on high dead snags scanning for reptiles and frogs.', 'Mid-morning thermals (09:00 - 12:00)', 'https://images.unsplash.com/photo-1611689342806-0863700ce1e4?w=600', 'Active'),
('Green Pit Viper', 'Craspedocephalus trigonocephalus', 'Reptile', 'Least Concern', 'Tropical wet evergreen rainforest foliage, tea plantations, and low bushes', 'Sinharaja', 'Carnivore', 'Endemic arboreal pit viper sporting striking lime green scales with black markings and a prehensile tail for grip.', 'Night walks or shaded low-hanging branches in damp forests', 'https://images.unsplash.com/photo-1531386151447-fd76ad50012f?w=600', 'Active');
END
END
GO