package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.service.UserService;
import com.drugprevention.gymbowlingbackend.repository.RoleRepository;
import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dev")
@Tag(name = "Development", description = "Development endpoints for testing")
public class DevController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DevController(UserService userService, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create-test-accounts")
    @Operation(summary = "Create test accounts", description = "Create test accounts for development")
    public ResponseEntity<Map<String, String>> createTestAccounts() {
        try {
            // Get roles
            Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
            Role staffRole = roleRepository.findByName("STAFF")
                .orElseThrow(() -> new RuntimeException("STAFF role not found"));
            Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

            // Delete existing test accounts if they exist
            userRepository.findByUsername("addmin").ifPresent(userRepository::delete);
            userRepository.findByUsername("stab").ifPresent(userRepository::delete);
            userRepository.findByUsername("khoa").ifPresent(userRepository::delete);

            // Create ADMIN account - with encoded password
            if (!userRepository.existsByUsername("addmin")) {
                String encodedPassword = passwordEncoder.encode("addmini");
                String fakeFirebaseUid = "traditional_admin_" + System.currentTimeMillis();
                User adminUser = new User("addmin", encodedPassword, fakeFirebaseUid, "addmin@gymbowling.com", "Admin User", "0901234567");
                adminUser.setRole(adminRole);
                adminUser.setIsActive(true);
                adminUser.setCreatedAt(java.time.LocalDateTime.now());
                adminUser.setUpdatedAt(java.time.LocalDateTime.now());
                userRepository.save(adminUser);
            }

            // Create STAFF account - with encoded password
            if (!userRepository.existsByUsername("stab")) {
                String encodedPassword = passwordEncoder.encode("123456");
                String fakeFirebaseUid = "traditional_staff_" + System.currentTimeMillis();
                User staffUser = new User("stab", encodedPassword, fakeFirebaseUid, "stab@gymbowling.com", "Staff User", "0901234568");
                staffUser.setRole(staffRole);
                staffUser.setIsActive(true);
                staffUser.setCreatedAt(java.time.LocalDateTime.now());
                staffUser.setUpdatedAt(java.time.LocalDateTime.now());
                userRepository.save(staffUser);
            }

            // Create USER account - with encoded password
            if (!userRepository.existsByUsername("khoa")) {
                String encodedPassword = passwordEncoder.encode("123456");
                String fakeFirebaseUid = "traditional_user_" + System.currentTimeMillis();
                User normalUser = new User("khoa", encodedPassword, fakeFirebaseUid, "khoa@gymbowling.com", "Khoa User", "0901234569");
                normalUser.setRole(userRole);
                normalUser.setIsActive(true);
                normalUser.setCreatedAt(java.time.LocalDateTime.now());
                normalUser.setUpdatedAt(java.time.LocalDateTime.now());
                userRepository.save(normalUser);
            }

            return ResponseEntity.ok(Map.of(
                "message", "Test accounts created successfully",
                "admin", "addmin/addmini",
                "staff", "stab/123456", 
                "user", "khoa/123456"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create test accounts: " + e.getMessage()));
        }
    }

    @GetMapping("/test-accounts")
    @Operation(summary = "List test accounts", description = "List all test accounts")
    public ResponseEntity<Map<String, Object>> listTestAccounts() {
        try {
            return ResponseEntity.ok(Map.of(
                "test_accounts", Map.of(
                    "admin", Map.of("username", "addmin", "password", "addmini", "role", "ADMIN"),
                    "staff", Map.of("username", "stab", "password", "123456", "role", "STAFF"),
                    "user", Map.of("username", "khoa", "password", "123456", "role", "USER")
                ),
                "note", "Use these accounts to test different roles"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to list test accounts: " + e.getMessage()));
        }
    }
}
