package com.safari.app.auth;

import com.safari.app.cottage_mgmt.dto.ApiResponse;
import com.safari.app.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Administrative user management endpoints.
 * Base Path: /api/users/**
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getUsers() {
        return ResponseEntity.ok(ApiResponse.ok("Users retrieved successfully", authService.getAllUsers()));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<User>> updateRole(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String role = body != null ? body.get("role") : null;
            User updated = authService.updateUserRole(id, role);
            return ResponseEntity.ok(ApiResponse.ok("User role updated successfully", updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            authService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.ok("User account deleted successfully", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }
}
