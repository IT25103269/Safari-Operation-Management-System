package com.safari.app.repository;

import com.safari.app.model.SafariPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafariPackageRepository extends JpaRepository<SafariPackage, Long> {
}
