package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.Role;
import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.repository.RoleRepository;
import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FirebaseAuth firebaseAuth;

    public UserController(UserRepository userRepository, 
                         RoleRepository roleRepository,
                         FirebaseAuth firebaseAuth) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.firebaseAuth = firebaseAuth;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            String confirmPassword = request.get("confirmPassword");
            String fullName = request.get("fullName");
            String phone = request.getOrDefault("phone", "");

            // Validation
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }
            if (password == null || password.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
            }
            if (!password.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Passwords do not match"));
            }

            // Check if email already exists
            if (userRepository.existsByEmail(email)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
            }

            // Create Firebase user
            UserRecord.CreateRequest firebaseRequest = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(fullName != null ? fullName : username)
                .setEmailVerified(false);

            UserRecord firebaseUser = firebaseAuth.createUser(firebaseRequest);

            // Create user in our database
            User user = new User(firebaseUser.getUid(), email, fullName, phone);
            
            // Set default role
            Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));
            user.setRole(userRole);

            user = userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "role", user.getRole().getName()
                )
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String usernameOrEmail = request.get("usernameOrEmail");
            String password = request.get("password");

            if (usernameOrEmail == null || password == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username/Email and password are required"));
            }

            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(usernameOrEmail);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not found"));
            }

            User user = userOpt.get();

            // Create custom token for login (Firebase will verify password on frontend)
            String customToken = firebaseAuth.createCustomToken(user.getFirebaseUid());
            
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "customToken", customToken,
                "user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "role", user.getRole().getName()
                ),
                "instructions", "Use customToken with Firebase signInWithCustomToken() to get idToken"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }
}
