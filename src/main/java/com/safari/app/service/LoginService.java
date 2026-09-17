package com.safari.app.service;

import com.safari.app.model.User;
import com.safari.app.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Safely authenticates user credentials without throwing uncaught exceptions.
     * Prevents 500 Whitelabel Errors by handling missing users cleanly.
     */
    public User authenticate(String email, String rawPassword) {
        if (email == null || email.trim().isEmpty() || rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Email and password must not be empty.");
        }

        // Safely fetch user by email using case-insensitive lookup
        User user = userRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        // Verify password hash safely against database
        if (user.getPasswordHash() != null) {
            String hash = user.getPasswordHash();
            if (hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$")) {
                try {
                    if (BCrypt.checkpw(rawPassword, hash)) {
                        return user;
                    }
                } catch (Exception ignored) {}
            } else if (hash.equals(rawPassword)) {
                return user;
            }
        }

        throw new IllegalArgumentException("Invalid email or password.");
    }

    /**
     * Resolves exact dashboard paths for Lanka Wild Trails system roles.
     * Prevents 404 Whitelabel Errors by normalizing strings and validating resource paths.
     */
    public String resolveDashboardUrl(String role) {
        if (role == null || role.trim().isEmpty()) {
            return "/login.html";
        }

        String r = role.trim().toLowerCase();
        if (r.contains("admin") || r.contains("coordinator") || r.contains("operations")) {
            return "/admin-dashboard.html";
        } else if (r.contains("cottage") || r.contains("manager")) {
            return "/cottage.html";
        } else if (r.contains("guide")) {
            return "/guide-dashboard.html";
        } else if (r.contains("driver")) {
            return "/driver-dashboard.html";
        } else if (r.contains("officer") || r.contains("wildlife")) {
            return "/view-sightings-review.html";
        } else if (r.contains("tourist")) {
            return "/tourist-dashboard.html";
        }
        return "/login.html";
    }
}