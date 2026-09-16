package com.safari.app.controller;

import com.safari.app.model.User;
import com.safari.app.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    // Direct HTTP request to the static HTML file
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "redirect:/register.html";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("userRole") String userRole,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {

        try {
            if (!password.equals(confirmPassword)) {
                return "redirect:/register.html?error=password_mismatch";
            }

            if (userRepository.existsByEmailIgnoreCase(email)) {
                return "redirect:/register.html?error=email_exists";
            }

            // Encrypt using BCrypt for security non-functional requirement
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            User newUser = new User(fullName, email, hashedPassword, userRole);
            userRepository.save(newUser);

            // Redirects to login page upon success
            return "redirect:/login.html?registered=true";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register.html?error=server_error";
        }
    }
}