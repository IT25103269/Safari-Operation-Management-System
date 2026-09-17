package com.safari.app.service;

import com.safari.app.model.SafariPackage;
import com.safari.app.repository.SafariPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SafariPackageService {

    @Autowired
    private SafariPackageRepository packageRepository;

    public List<SafariPackage> getAllPackages(String destination, Double maxPrice, String query, Boolean activeOnly) {
        List<SafariPackage> all = packageRepository.findAll();

        return all.stream()
                .filter(p -> {
                    if (activeOnly != null && activeOnly) {
                        if (!"Active".equalsIgnoreCase(p.getStatus())) return false;
                    }

                    if (destination != null && !destination.trim().isEmpty() && !destination.equalsIgnoreCase("all")) {
                        if (p.getDestination() == null || !p.getDestination().equalsIgnoreCase(destination.trim())) {
                            return false;
                        }
                    }

                    if (maxPrice != null && maxPrice > 0) {
                        if (p.getPricePerPerson() == null || p.getPricePerPerson() > maxPrice) {
                            return false;
                        }
                    }

                    if (query != null && !query.trim().isEmpty()) {
                        String q = query.toLowerCase().trim();
                        boolean nameMatch = p.getPackageName() != null && p.getPackageName().toLowerCase().contains(q);
                        boolean destMatch = p.getDestination() != null && p.getDestination().toLowerCase().contains(q);
                        boolean sumMatch = p.getItinerarySummary() != null && p.getItinerarySummary().toLowerCase().contains(q);
                        boolean svcMatch = p.getIncludedServices() != null && p.getIncludedServices().toLowerCase().contains(q);
                        if (!nameMatch && !destMatch && !sumMatch && !svcMatch) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    public SafariPackage getPackageById(Integer id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Safari Package not found with ID: " + id));
    }

    public SafariPackage createPackage(SafariPackage safariPackage) {
        validatePackage(safariPackage);
        if (safariPackage.getStatus() == null || safariPackage.getStatus().trim().isEmpty()) {
            safariPackage.setStatus("Active");
        }
        return packageRepository.save(safariPackage);
    }

    public SafariPackage updatePackage(Integer id, SafariPackage updated) {
        SafariPackage existing = getPackageById(id);
        validatePackage(updated);

        existing.setPackageName(updated.getPackageName().trim());
        existing.setDestination(updated.getDestination().trim());
        existing.setDurationDays(updated.getDurationDays());
        existing.setPricePerPerson(updated.getPricePerPerson());
        existing.setMaxGroupSize(updated.getMaxGroupSize());
        existing.setItinerarySummary(updated.getItinerarySummary());
        existing.setIncludedServices(updated.getIncludedServices());
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }

        return packageRepository.save(existing);
    }

    public SafariPackage toggleArchive(Integer id) {
        SafariPackage existing = getPackageById(id);
        if ("Active".equalsIgnoreCase(existing.getStatus())) {
            existing.setStatus("Archived");
        } else {
            existing.setStatus("Active");
        }
        return packageRepository.save(existing);
    }

    public void deletePackage(Integer id) {
        SafariPackage existing = getPackageById(id);
        packageRepository.delete(existing);
    }

    private void validatePackage(SafariPackage pkg) {
        if (pkg.getPackageName() == null || pkg.getPackageName().trim().isEmpty()) {
            throw new IllegalArgumentException("Package name is required.");
        }
        if (pkg.getDestination() == null || pkg.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("Destination national park is required.");
        }
        if (pkg.getDurationDays() == null || pkg.getDurationDays() <= 0) {
            throw new IllegalArgumentException("Duration days must be greater than zero.");
        }
        if (pkg.getPricePerPerson() == null || pkg.getPricePerPerson() < 0) {
            throw new IllegalArgumentException("Price per person cannot be negative.");
        }
        if (pkg.getMaxGroupSize() == null || pkg.getMaxGroupSize() <= 0) {
            throw new IllegalArgumentException("Max group size must be greater than zero.");
        }
    }
}
