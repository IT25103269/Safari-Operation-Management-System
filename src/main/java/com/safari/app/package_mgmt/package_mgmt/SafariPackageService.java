package com.safari.app.package_mgmt;

import com.safari.app.model.PackageItinerary;
import com.safari.app.model.SafariPackage;
import com.safari.app.package_mgmt.dto.PackageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SafariPackageService {

    private final SafariPackageRepository packageRepository;

    @Autowired
    public SafariPackageService(SafariPackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Transactional(readOnly = true)
    public List<SafariPackage> getAvailablePackages(String park, BigDecimal minPrice, BigDecimal maxPrice) {
        return getPackages(park, minPrice, maxPrice, false);
    }

    @Transactional(readOnly = true)
    public List<SafariPackage> getPackages(String park, BigDecimal minPrice, BigDecimal maxPrice, boolean includeArchived) {
        if (includeArchived) {
            return packageRepository.findAll();
        }

        String parkFilter = (park != null && !park.trim().isEmpty()) ? park.trim() : null;
        return packageRepository.searchPackagesWithFilters(parkFilter, minPrice, maxPrice);
    }

    @Transactional(readOnly = true)
    public List<SafariPackage> getAllPackages() {
        return packageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SafariPackage getPackageById(Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Safari package not found with ID: " + id));
    }

    public SafariPackage createPackage(PackageRequestDTO req) {
        SafariPackage pkg = new SafariPackage();
        pkg.setPackageCode("PKG-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        pkg.setTitle(req.getTitle());
        pkg.setNationalPark(req.getNationalPark());
        pkg.setDurationDays(req.getDurationDays());
        pkg.setPricePerPerson(req.getPricePerPerson());
        pkg.setMaxGroupSize(req.getMaxGroupSize());
        pkg.setDescription(req.getDescription());
        pkg.setIncludedServices(req.getIncludedServices());
        pkg.setStatus("ACTIVE");

        pkg.setImageUrl(req.getImageUrl());


        if (req.getItineraries() != null) {
            for (PackageRequestDTO.ItineraryDTO item : req.getItineraries()) {
                PackageItinerary itinerary = new PackageItinerary(
                        item.getDayNumber(), item.getTimeSlot(), item.getActivityTitle(), item.getDescription()
                );
                pkg.addItinerary(itinerary);
            }
        }

        return packageRepository.save(pkg);
    }

    public SafariPackage updatePackage(Long id, PackageRequestDTO req) {
        SafariPackage existing = getPackageById(id);
        existing.setTitle(req.getTitle());
        existing.setNationalPark(req.getNationalPark());
        existing.setDurationDays(req.getDurationDays());
        existing.setPricePerPerson(req.getPricePerPerson());
        existing.setMaxGroupSize(req.getMaxGroupSize());
        existing.setDescription(req.getDescription());
        existing.setIncludedServices(req.getIncludedServices());

        if (req.getItineraries() != null) {
            existing.getItineraries().clear();
            for (PackageRequestDTO.ItineraryDTO item : req.getItineraries()) {
                PackageItinerary itinerary = new PackageItinerary(
                        item.getDayNumber(), item.getTimeSlot(), item.getActivityTitle(), item.getDescription()
                );
                existing.addItinerary(itinerary);
            }
        }

        return packageRepository.save(existing);
    }

    public SafariPackage archivePackage(Long id) {
        SafariPackage pkg = getPackageById(id);
        pkg.setStatus("ARCHIVED");
        return packageRepository.save(pkg);
    }


    // --- CRUD: DELETE package ---
    public void deletePackage(Long id) {
        SafariPackage existing = getPackageById(id);
        try {
            packageRepository.delete(existing);
            packageRepository.flush();
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new IllegalStateException(
                    "Package cannot be deleted because it is referenced by an existing resource allocation. " +
                            "Archive it instead or remove the allocation first.");
        }
    }

}
