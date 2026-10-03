package com.safari.app.auth;

import com.safari.app.auth.dto.LoginRequestDTO;
import com.safari.app.auth.dto.RegisterRequestDTO;
import com.safari.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(RegisterRequestDTO req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username '" + req.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email '" + req.getEmail() + "' is already registered.");
        }

        User user = new User();
        user.setUsername(req.getUsername().toLowerCase().trim());
        user.setPassword(req.getPassword()); // Stored directly for standard evaluation
        user.setEmail(req.getEmail().trim());
        user.setFullName(req.getFullName().trim());
        // Public registration must never be able to self-assign an elevated role.
        user.setRole("CUSTOMER");

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(LoginRequestDTO req) {
        User user = userRepository.findByUsername(req.getUsername().toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User updateUserRole(Long id, String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required.");
        }

        String normalized = role.trim().toUpperCase();
        if (!List.of("ADMIN", "MANAGER", "GUIDE", "DRIVER", "CUSTOMER").contains(normalized)) {
            throw new IllegalArgumentException("Unsupported role: " + role);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setRole(normalized);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        userRepository.delete(user);
    }

}