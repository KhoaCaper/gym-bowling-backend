package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.AuthDTO;
import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.service.UserService;
import com.drugprevention.gymbowlingbackend.security.JwtTokenProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
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
 * Firebase Authentication Controller
 * Handles Google Firebase authentication
 * 
 * @author GymBo Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/firebase-auth")
@Tag(name = "Firebase Authentication", description = "Google Firebase authentication endpoints")
@CrossOrigin(origins = "*")
public class FirebaseAuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthController(
            UserService userService, 
            JwtTokenProvider jwtTokenProvider,
            FirebaseAuth firebaseAuth
    ) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.firebaseAuth = firebaseAuth;
    }

    /**
     * Firebase Login
     * Authenticate user with Firebase ID token
     * 
     * @param request Firebase authentication request
     * @return JWT token and user information
     */
    @PostMapping("/login")
    @Operation(
        summary = "Firebase Login", 
        description = "Authenticate user with Firebase ID token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login successful",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid Firebase token",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication failed",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))
        )
    })
    public ResponseEntity<AuthDTO> login(@Valid @RequestBody FirebaseLoginRequest request) {
        try {
            if (firebaseAuth == null) {
                return ResponseEntity.status(503)
                    .body(new AuthDTO("Firebase service is not available"));
            }
            
            if (request.getIdToken() == null || request.getIdToken().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new AuthDTO("Firebase ID token is required"));
            }

            // Verify Firebase token
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(request.getIdToken());
            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String displayName = decodedToken.getName();

            // Find or create user
            User user = userService.findByFirebaseUid(firebaseUid)
                .orElseGet(() -> {
                    // Create new user if not exists
                    return userService.createOrUpdateUser(firebaseUid, email, displayName, null);
                });

            // Check if user is active
            if (!user.getIsActive()) {
                return ResponseEntity.status(401)
                    .body(new AuthDTO("User account is deactivated"));
            }

            // Generate JWT token
            String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());

            // Build response
            AuthDTO.UserInfo userInfo = buildUserInfo(user);
            AuthDTO response = new AuthDTO("Firebase login successful", token, userInfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401)
                .body(new AuthDTO("Firebase authentication failed: " + e.getMessage()));
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
        userInfo.setProvider("Firebase");
        return userInfo;
    }

    /**
     * Firebase Login Request DTO
     */
    public static class FirebaseLoginRequest {
        private String idToken;

        // Constructors
        public FirebaseLoginRequest() {}
        
        public FirebaseLoginRequest(String idToken) {
            this.idToken = idToken;
        }

        // Getters and Setters
        public String getIdToken() { return idToken; }
        public void setIdToken(String idToken) { this.idToken = idToken; }
    }
}
