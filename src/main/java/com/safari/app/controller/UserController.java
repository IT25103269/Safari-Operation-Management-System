package com.safari.app.controller;

import com.safari.app.model.User;
import com.safari.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("LOGGED_IN_USER_ID");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not logged in"));
        }

        User user = userService.getUserById(userId);
        return ResponseEntity.ok(Map.of(
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "role", user.getRole()
        ));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> payload, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("LOGGED_IN_USER_ID");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not logged in"));
        }

        try {
            userService.updateUserProfile(userId, payload.get("fullName"), payload.get("email"));
            session.setAttribute("LOGGED_IN_USER_EMAIL", payload.get("email"));
            return ResponseEntity.ok(Map.of("message", "Profile updated successfully!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("LOGGED_IN_USER_ID");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not logged in"));
        }

        try {
            userService.changePassword(userId, payload.get("currentPassword"), payload.get("newPassword"));
            return ResponseEntity.ok(Map.of("message", "Password changed successfully!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllUsersAdmin() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/admin/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        try {
            String newRole = payload.get("role");
            userService.updateUserRole(id, newRole);
            return ResponseEntity.ok(Map.of("success", true, "message", "User role updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/admin/update-role-form")
    public ResponseEntity<?> updateRoleForm(
            @RequestParam("userId") String userIdStr,
            @RequestParam("newRole") String newRole,
            jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        try {
            // Strip any USR- prefix if passed
            String cleanId = userIdStr.replaceAll("[^0-9]", "");
            Integer uid = Integer.parseInt(cleanId);
            userService.updateUserRole(uid, newRole);
            response.sendRedirect("/admin-user-management.html?success=true");
            return null;
        } catch (Exception e) {
            response.sendRedirect("/admin-user-management.html?error=failed");
            return null;
        }
    }
}