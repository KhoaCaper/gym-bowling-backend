package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.AuthDTO;
import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.service.UserService;
import com.drugprevention.gymbowlingbackend.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

/**
 * Authentication Controller
 * Handles user authentication and registration
 * 
 * @author GymBo Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Traditional Login
     * Authenticate user with username and password
     * 
     * @param request Login credentials
     * @return JWT token and user information
     */
    @PostMapping("/login")
    @Operation(
        summary = "Traditional Login", 
        description = "Authenticate user with username and password"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login successful",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid credentials or validation failed",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication failed",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        )
    })
    public ResponseEntity<AuthDTO> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Validate input
            if (request.getUsername() == null || request.getPassword() == null || 
                request.getUsername().trim().isEmpty() || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new AuthDTO("Username and password are required"));
            }

            // Verify credentials
            if (!userService.verifyPassword(request.getUsername(), request.getPassword())) {
                return ResponseEntity.status(401)
                    .body(new AuthDTO("Invalid username or password"));
            }

            // Get user information
            User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if user is active
            if (!user.getIsActive()) {
                return ResponseEntity.status(401)
                    .body(new AuthDTO("User account is deactivated"));
            }

            // Generate JWT token
            String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());

            // Build response
            AuthDTO.UserInfo userInfo = buildUserInfo(user);
            AuthDTO response = new AuthDTO("Login successful", token, userInfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthDTO("Login failed: " + e.getMessage()));
        }
    }

    /**
     * User Registration
     * Register new user with username and password
     * 
     * @param request Registration information
     * @return User information
     */
    @PostMapping("/register")
    @Operation(
        summary = "User Registration", 
        description = "Register new user with username and password"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Registration successful",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation failed or user already exists",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        )
    })
    public ResponseEntity<AuthDTO> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Validate input
            if (request.getUsername() == null || request.getPassword() == null || 
                request.getEmail() == null || request.getFullName() == null) {
                return ResponseEntity.badRequest()
                    .body(new AuthDTO("Username, password, email, and fullName are required"));
            }

            if (request.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                    .body(new AuthDTO("Password must be at least 6 characters"));
            }

            // Create new user
            User newUser = userService.createTraditionalUser(
                request.getUsername(), 
                request.getPassword(), 
                request.getEmail(), 
                request.getFullName(), 
                request.getPhone()
            );

            // Build response
            AuthDTO.UserInfo userInfo = buildUserInfo(newUser);
            AuthDTO response = new AuthDTO("Registration successful", null, userInfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthDTO("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Get Current User Information
     * Retrieve current authenticated user information
     * 
     * @param authHeader Authorization header with JWT token
     * @return Current user information
     */
    @GetMapping("/me")
    @Operation(
        summary = "Get Current User", 
        description = "Get current authenticated user information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User information retrieved successfully",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Invalid or expired token",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        )
    })
    public ResponseEntity<AuthDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                    .body(new AuthDTO("Invalid authorization header"));
            }

            String token = authHeader.substring(7); // Remove "Bearer "
            
            // Validate token
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(401)
                    .body(new AuthDTO("Invalid or expired token"));
            }

            // Get username from token
            String username = jwtTokenProvider.getUsernameFromToken(token);
            
            // Find user
            User user = userService.findByUsername(username)
                .orElseGet(() -> {
                    // Create test user if not exists
                    return userService.createTraditionalUser(
                        "test_user", 
                        "test123", 
                        "test@example.com", 
                        "Test User", 
                        "123-456-7890"
                    );
                });

            // Build response
            AuthDTO.UserInfo userInfo = buildUserInfo(user);
            AuthDTO response = new AuthDTO("User information retrieved successfully", null, userInfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401)
                .body(new AuthDTO("Failed to get user information: " + e.getMessage()));
        }
    }

    /**
     * Build UserInfo object from User entity
     */
    private AuthDTO.UserInfo buildUserInfo(User user) {
        AuthDTO.UserInfo userInfo = new AuthDTO.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setEmail(user.getEmail());
        userInfo.setFullName(user.getFullName());
        userInfo.setPhone(user.getPhone() != null ? user.getPhone() : "");
        userInfo.setRole(user.getRole().getName());
        userInfo.setProvider(user.getFirebaseUid() != null ? "Firebase" : "Traditional");
        return userInfo;
    }

    /**
     * Login Request DTO
     */
    public static class LoginRequest {
        private String username;
        private String password;

        // Constructors
        public LoginRequest() {}
        
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    /**
     * Register Request DTO
     */
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private String fullName;
        private String phone;

        // Constructors
        public RegisterRequest() {}

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
}
