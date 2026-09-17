package com.safari.app.config;

import com.safari.app.model.*;
import com.safari.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final SafariPackageRepository packageRepository;
    private final WildlifeRepository speciesRepository;
    private final WildlifeSightingRepository sightingRepository;
    private final CottageRepository cottageRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public DatabaseDataInitializer(
            UserRepository userRepository,
            VehicleRepository vehicleRepository,
            SafariPackageRepository packageRepository,
            WildlifeRepository speciesRepository,
            WildlifeSightingRepository sightingRepository,
            CottageRepository cottageRepository,
            BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.packageRepository = packageRepository;
        this.speciesRepository = speciesRepository;
        this.sightingRepository = sightingRepository;
        this.cottageRepository = cottageRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedVehicles();
        seedPackages();
        seedWildlifeSpecies();
        seedSightings();
        seedCottages();
        seedBookings();
    }

    private void seedUsers() {
        seedUserIfNotExists("System Administrator", "admin@email.com", "123", "admin");
        seedUserIfNotExists("Lodge General Manager", "manager@email.com", "123", "manager");
        seedUserIfNotExists("Kasun Perera", "driver@email.com", "123", "driver");
        seedUserIfNotExists("Sunil Jayawardena", "guide@email.com", "123", "guide");
        seedUserIfNotExists("Ranger Nimal Bandara", "officer@email.com", "123", "officer");
        seedUserIfNotExists("Sarah Jenkins", "tourist@email.com", "123", "tourist");
    }

    private void seedUserIfNotExists(String name, String email, String password, String role) {
        if (!userRepository.existsByEmailIgnoreCase(email)) {
            String hash = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
            User u = new User(name, email, hash, role);
            userRepository.save(u);
        }
    }

    private void seedVehicles() {
        if (vehicleRepository.count() == 0) {
            vehicleRepository.save(new Vehicle("WP CAB-1234", "Safari Jeep (Toyota Hilux 4x4)", 6, "Kasun Perera", "AVAILABLE", "Equipped with pop-up roof for wildlife viewing"));
            vehicleRepository.save(new Vehicle("NC-8822", "Land Cruiser Safari Edition", 8, "Kasun Perera", "AVAILABLE", "Heavy duty 4WD fitted with high ground clearance"));
            vehicleRepository.save(new Vehicle("CP VAN-7788", "Safari Minibus", 12, "Kasun Perera", "AVAILABLE", "Spacious passenger van for group transfers and scenic tours"));
            vehicleRepository.save(new Vehicle("SP JEP-5544", "Defender Safari Soft-top", 4, null, "UNDER_MAINTENANCE", "Suspension inspection in progress"));
        }
    }

    private void seedPackages() {
        if (packageRepository.count() == 0) {
            SafariPackage p1 = new SafariPackage();
            p1.setPackageName("Yala Leopard & Wildlife Expedition");
            p1.setDestination("Yala");
            p1.setDurationDays(3);
            p1.setPricePerPerson(350.00);
            p1.setMaxGroupSize(6);
            p1.setItinerarySummary("Day 1: Arrival and evening game drive. Day 2: Full day leopard tracking in Block 1. Day 3: Morning coastal wildlife drive and departure.");
            p1.setIncludedServices("Park entrance fees, 4x4 Jeep with tracker, 3 meals daily, luxury tented camp stay");
            p1.setStatus("Active");
            packageRepository.save(p1);

            SafariPackage p2 = new SafariPackage();
            p2.setPackageName("Udawalawe Elephant Discovery");
            p2.setDestination("Udawalawe");
            p2.setDurationDays(2);
            p2.setPricePerPerson(220.00);
            p2.setMaxGroupSize(8);
            p2.setItinerarySummary("Day 1: Afternoon safari around the reservoir. Day 2: Visit to Elephant Transit Home feeding and morning trail.");
            p2.setIncludedServices("Safari Jeep transfer, tracker guide, park entrance, buffet lunch");
            p2.setStatus("Active");
            packageRepository.save(p2);

            SafariPackage p3 = new SafariPackage();
            p3.setPackageName("Wilpattu Ancient Wilderness Trail");
            p3.setDestination("Wilpattu");
            p3.setDurationDays(4);
            p3.setPricePerPerson(480.00);
            p3.setMaxGroupSize(6);
            p3.setItinerarySummary("Day 1: Arrival & Willu lakes tour. Day 2: Deep jungle sloth bear tracking. Day 3: Archeological ruins safari. Day 4: Morning bird watching.");
            p3.setIncludedServices("Forest bungalow stay, specialized naturalist, 4x4 transport, all permits");
            p3.setStatus("Active");
            packageRepository.save(p3);
        }
    }

    private void seedWildlifeSpecies() {
        if (speciesRepository.count() == 0) {
            WildlifeSpecies sp1 = new WildlifeSpecies(
                    "Sri Lankan Leopard",
                    "Panthera pardus kotiya",
                    "Mammal",
                    "Endangered",
                    "Dry lowland forest and scrub jungle",
                    "Yala",
                    "Carnivore",
                    "The apex predator of Sri Lanka, known for its distinct rosette coat and solitary habits.",
                    "Early morning (6:00 AM - 8:30 AM) and late afternoon (4:30 PM - 6:30 PM)",
                    "https://images.unsplash.com/photo-1561731216-c3a4d99437d5?auto=format&fit=crop&w=600&q=80"
            );
            speciesRepository.save(sp1);

            WildlifeSpecies sp2 = new WildlifeSpecies(
                    "Sri Lankan Elephant",
                    "Elephas maximus maximus",
                    "Mammal",
                    "Endangered",
                    "Grasslands, scrub forest, and reservoir margins",
                    "Udawalawe",
                    "Herbivore",
                    "The largest of the Asian elephant subspecies, often seen in large herds near water reservoirs.",
                    "Afternoon (3:00 PM - 6:00 PM)",
                    "https://images.unsplash.com/photo-1557050543-4d5f4e07ef46?auto=format&fit=crop&w=600&q=80"
            );
            speciesRepository.save(sp2);

            WildlifeSpecies sp3 = new WildlifeSpecies(
                    "Sri Lankan Sloth Bear",
                    "Melursus ursinus inornatus",
                    "Mammal",
                    "Vulnerable",
                    "Dense dry-zone monsoon forests",
                    "Wilpattu",
                    "Omnivore",
                    "Shy and nocturnal, known for foraging on Palu and Weera tree berries and termite mounds.",
                    "Dawn and dusk along gravel forest tracks",
                    "https://images.unsplash.com/photo-1589656966895-2f33e7653819?auto=format&fit=crop&w=600&q=80"
            );
            speciesRepository.save(sp3);

            WildlifeSpecies sp4 = new WildlifeSpecies(
                    "Mugger Crocodile",
                    "Crocodylus palustris",
                    "Reptile",
                    "Vulnerable",
                    "Freshwater tanks, slow-moving rivers, and lagoons",
                    "Bundala",
                    "Carnivore",
                    "Large freshwater crocodilian frequently seen basking along tank bunds in dry zone parks.",
                    "Mid-day sun basking (10:00 AM - 2:00 PM)",
                    "https://images.unsplash.com/photo-1527525443983-6e60c75fff46?auto=format&fit=crop&w=600&q=80"
            );
            speciesRepository.save(sp4);
        }
    }

    private void seedSightings() {
        if (sightingRepository.count() == 0) {
            WildlifeSighting s1 = new WildlifeSighting();
            s1.setTripRef("TRIP-101");
            s1.setSpeciesName("Sri Lankan Leopard");
            s1.setSightingDate(LocalDate.now().minusDays(1));
            s1.setSightingTime("07:45 AM");
            s1.setParkLocation("Yala National Park");
            s1.setSpecificLocation("Block 1, Kotademuwa rocky outcrop");
            s1.setPhotoUrl("https://images.unsplash.com/photo-1561731216-c3a4d99437d5?auto=format&fit=crop&w=600&q=80");
            s1.setNotes("Young male resting calmly on the sun-warmed rocks before descending into the shrub.");
            s1.setLoggedBy("Sunil Jayawardena (Guide)");
            s1.setStatus("Verified");
            sightingRepository.save(s1);

            WildlifeSighting s2 = new WildlifeSighting();
            s2.setTripRef("TRIP-102");
            s2.setSpeciesName("Asian Elephant Herd");
            s2.setSightingDate(LocalDate.now().minusDays(2));
            s2.setSightingTime("04:15 PM");
            s2.setParkLocation("Udawalawe National Park");
            s2.setSpecificLocation("Near reservoir embankment sector 3");
            s2.setPhotoUrl("https://images.unsplash.com/photo-1557050543-4d5f4e07ef46?auto=format&fit=crop&w=600&q=80");
            s2.setNotes("Family of 8 elephants including 2 calves drinking and mud-bathing.");
            s2.setLoggedBy("Kasun Perera (Driver)");
            s2.setStatus("Verified");
            sightingRepository.save(s2);

            WildlifeSighting s3 = new WildlifeSighting();
            s3.setTripRef("TRIP-103");
            s3.setSpeciesName("Sloth Bear");
            s3.setSightingDate(LocalDate.now());
            s3.setSightingTime("06:30 AM");
            s3.setParkLocation("Wilpattu National Park");
            s3.setSpecificLocation("Palu forest trail junction");
            s3.setPhotoUrl("https://images.unsplash.com/photo-1589656966895-2f33e7653819?auto=format&fit=crop&w=600&q=80");
            s3.setNotes("Single adult bear foraging near fallen logs.");
            s3.setLoggedBy("Sunil Jayawardena (Guide)");
            s3.setStatus("Pending");
            sightingRepository.save(s3);
        }
    }

    private void seedCottages() {
        if (cottageRepository.count() == 0) {
            cottageRepository.save(new Cottage(
                    "COT-101",
                    "Leopard Rock Villa",
                    "Yala Buffer Zone",
                    "LUXURY_SUITE",
                    2,
                    180.00,
                    "WiFi, AC, Private Deck, Plunge Pool",
                    "Perched above the granite outcrop our trackers call Leopard Rock, with panoramic views of the buffer ridge."
            ));

            cottageRepository.save(new Cottage(
                    "COT-102",
                    "Elephant Corridor Cottage",
                    "Yala Buffer Zone",
                    "DELUXE",
                    3,
                    150.00,
                    "WiFi, AC, Garden View, Mini Bar",
                    "Set forty metres back from the restored elephant corridor with early morning wildlife watching directly from the private deck."
            ));

            cottageRepository.save(new Cottage(
                    "COT-103",
                    "Canopy Deluxe Cottage",
                    "Wilpattu Buffer",
                    "DELUXE",
                    2,
                    130.00,
                    "WiFi, Fan Cooling, Private Deck",
                    "Raised on teak stilts beneath the ancient forest canopy, ideal for bird watchers and nature enthusiasts."
            ));

            cottageRepository.save(new Cottage(
                    "COT-104",
                    "Riverside Family Chalet",
                    "Udawalawe Buffer",
                    "FAMILY_CHALET",
                    5,
                    210.00,
                    "WiFi, AC, Two Bedrooms, Kitchenette",
                    "Two spacious bedrooms facing the seasonal river bend, built comfortably for families or small safari groups."
            ));
        }
    }

    private void seedBookings() {
        if (bookingRepository.count() == 0) {
            SafariBooking b1 = new SafariBooking();
            b1.setBookingRef("LWT-2026-1001");
            b1.setTouristName("Sarah Jenkins");
            b1.setTouristEmail("tourist@email.com");
            b1.setTouristPhone("+94 77 123 4567");
            b1.setPackageName("Yala Leopard & Wildlife Expedition");
            b1.setDestination("Yala");
            b1.setTravelDate(LocalDate.now().plusDays(5));
            b1.setParticipantsCount(4);
            b1.setTotalPrice(1400.00);
            b1.setStatus("CONFIRMED");
            bookingRepository.save(b1);

            SafariBooking b2 = new SafariBooking();
            b2.setBookingRef("LWT-2026-1002");
            b2.setTouristName("David Miller");
            b2.setTouristEmail("david.m@example.com");
            b2.setTouristPhone("+44 7911 123456");
            b2.setPackageName("Udawalawe Elephant Discovery");
            b2.setDestination("Udawalawe");
            b2.setTravelDate(LocalDate.now().plusDays(2));
            b2.setParticipantsCount(6);
            b2.setTotalPrice(1320.00);
            b2.setStatus("ALLOCATED");
            b2.setAssignedGuide("Sunil Jayawardena");
            b2.setAssignedVehicle("NC-8822");
            bookingRepository.save(b2);
        }
    }
}
