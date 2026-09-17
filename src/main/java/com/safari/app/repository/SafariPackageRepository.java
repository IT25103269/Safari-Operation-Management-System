package com.safari.app.repository;

import com.safari.app.model.SafariPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafariPackageRepository extends JpaRepository<SafariPackage, Integer> {
    List<SafariPackage> findByStatusIgnoreCase(String status);
    List<SafariPackage> findByDestinationIgnoreCaseAndStatusIgnoreCase(String destination, String status);
}
