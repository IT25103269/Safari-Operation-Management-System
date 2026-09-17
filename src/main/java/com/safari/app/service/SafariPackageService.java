package com.safari.app.service;

import com.safari.app.model.SafariPackage;
import com.safari.app.repository.SafariPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SafariPackageService {

    @Autowired
    private SafariPackageRepository safariPackageRepository;

    public List<SafariPackage> getAllPackages() {
        return safariPackageRepository.findAll();
    }

    public SafariPackage getPackageById(Long id) {
        return safariPackageRepository.findById(id).orElse(null);
    }

    public SafariPackage savePackage(SafariPackage safariPackage) {
        return safariPackageRepository.save(safariPackage);
    }

    public void deletePackage(Long id) {
        safariPackageRepository.deleteById(id);
    }
}

