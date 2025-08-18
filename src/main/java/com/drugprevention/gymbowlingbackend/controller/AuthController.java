package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String phone = request.getOrDefault("phone", "");
            
            User user = userService.verifyAndGetUser(token);
            
            // Update phone if provided
            if (phone != null && !phone.isEmpty()) {
                user.setPhone(phone);
                user = userService.createOrUpdateUser(
                    user.getFirebaseUid(), 
                    user.getUsername(),
                    user.getEmail(), 
                    user.getFullName(), 
                    phone
                );
            }
            
            return ResponseEntity.ok(Map.of(
                "message", "Login successful with " + (user.getEmail().contains("gmail") ? "Gmail" : "Email"),
                "user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "phone", user.getPhone() != null ? user.getPhone() : "",
                    "role", user.getRole().getName(),
                    "provider", user.getEmail().contains("gmail") ? "Google" : "Email"
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String phone = request.get("phone");
            
            User user = userService.verifyAndGetUser(token);
            
            if (phone != null && !phone.isEmpty()) {
                user.setPhone(phone);
                user = userService.createOrUpdateUser(
                    user.getFirebaseUid(), 
                    user.getUsername(),
                    user.getEmail(), 
                    user.getFullName(), 
                    phone
                );
            }
            
            return ResponseEntity.ok(Map.of(
                "message", "Registration successful",
                "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "phone", user.getPhone() != null ? user.getPhone() : "",
                    "role", user.getRole().getName()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            User user = userService.verifyAndGetUser(token);
            
            return ResponseEntity.ok(Map.of(
                "user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "phone", user.getPhone() != null ? user.getPhone() : "",
                    "role", user.getRole().getName()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get user info: " + e.getMessage()));
        }
    }
}
