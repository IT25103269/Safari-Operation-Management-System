package com.safari.app.service;

import com.safari.app.model.User;
import com.safari.app.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    public void updateUserProfile(Integer userId, String newFullName, String newEmail) {
        User user = getUserById(userId);

        if (newFullName == null || newFullName.trim().isEmpty() || newEmail == null || newEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name and email cannot be empty.");
        }

        // Check email uniqueness if email is changed
        if (!user.getEmail().equalsIgnoreCase(newEmail.trim())) {
            Optional<User> existing = userRepository.findByEmailIgnoreCase(newEmail.trim());
            if (existing.isPresent()) {
                throw new IllegalArgumentException("This email is already in use by another account.");
            }
            user.setEmail(newEmail.trim());
        }

        user.setFullName(newFullName.trim());
        userRepository.save(user);
    }

    public void changePassword(Integer userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);

        if (currentPassword == null || newPassword == null || newPassword.trim().length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long.");
        }

        if (!BCrypt.checkpw(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Incorrect current password.");
        }

        String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPasswordHash(newHashedPassword);
        userRepository.save(user);
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserRole(Integer userId, String newRole) {
        User user = getUserById(userId);
        if (newRole == null || newRole.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
        user.setRole(newRole.trim());
        userRepository.save(user);
    }
}