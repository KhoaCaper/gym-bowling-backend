package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.service.UserService;
import com.drugprevention.gymbowlingbackend.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public TestController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/create-test-user")
    public ResponseEntity<?> createTestUser(@RequestBody Map<String, String> request) {
        try {
            String email = request.getOrDefault("email", "test@gym.com");
            String fullName = request.getOrDefault("fullName", "Test User");
            String phone = request.getOrDefault("phone", "0123456789");
            String role = request.getOrDefault("role", "USER");
            
            // Create user with fake Firebase UID for testing
            String fakeFirebaseUid = "test-uid-" + System.currentTimeMillis();
            
            User user = userService.createOrUpdateUser(fakeFirebaseUid, email, fullName, phone);
            
            // Set role using Role entity
            Role roleEntity = roleRepository.findByName(role.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found: " + role));
            user.setRole(roleEntity);
            
            return ResponseEntity.ok(Map.of(
                "message", "Test user created successfully",
                "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "role", user.getRole().getName(),
                    "firebaseUid", user.getFirebaseUid()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create test user: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            // This would normally require admin access, but for testing we'll allow it
            return ResponseEntity.ok(Map.of(
                "message", "This is a test endpoint - normally would require authentication",
                "note", "Use this to verify backend is working without Firebase"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/mock-login")
    public ResponseEntity<?> mockLogin(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            
            Optional<User> userOpt = userService.findByEmail(email);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return ResponseEntity.ok(Map.of(
                    "message", "Mock login successful (no Firebase needed)",
                    "user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "fullName", user.getFullName(),
                        "role", user.getRole().toString()
                    )
                ));
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not found with email: " + email));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Mock login failed: " + e.getMessage()));
        }
    }
}
