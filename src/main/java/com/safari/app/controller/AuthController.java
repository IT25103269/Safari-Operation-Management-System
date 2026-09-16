package com.safari.app.controller;

import com.safari.app.model.User;
import com.safari.app.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session) {

        try {

            User authenticatedUser = loginService.authenticate(email, password);


            session.setAttribute("LOGGED_IN_USER_ID", authenticatedUser.getUserId());
            session.setAttribute("LOGGED_IN_USER_EMAIL", authenticatedUser.getEmail());
            session.setAttribute("USER_ROLE", authenticatedUser.getRole());

            String targetDashboard = loginService.resolveDashboardUrl(authenticatedUser.getRole());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "redirectUrl", targetDashboard,
                    "userName", authenticatedUser.getFullName()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> handleLogout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
    }
}