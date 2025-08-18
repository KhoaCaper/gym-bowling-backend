package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.AuthDTO;
import com.drugprevention.gymbowlingbackend.dto.LoginDTO;
import com.drugprevention.gymbowlingbackend.dto.RegisterDTO;
import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login with Firebase token", 
               description = "Authenticate user with Firebase ID token")
    @ApiResponse(responseCode = "200", description = "Login successful",
                content = @Content(schema = @Schema(implementation = AuthDTO.class)))
    @ApiResponse(responseCode = "400", description = "Login failed",
                content = @Content(schema = @Schema(implementation = AuthDTO.class)))
    public ResponseEntity<AuthDTO> login(@RequestBody LoginDTO request) {
        try {
            String token = request.getToken();
            String phone = request.getPhone() != null ? request.getPhone() : "";
            
            User user = userService.verifyAndGetUser(token);
            
            // Update phone if provided
            if (phone != null && !phone.isEmpty()) {
                user.setPhone(phone);
                user = userService.createOrUpdateUser(
                    user.getFirebaseUid(), 
                    user.getEmail(), 
                    user.getFullName(), 
                    phone
                );
            }
            
            // Create response
            AuthDTO.UserInfo userInfo = new AuthDTO.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setEmail(user.getEmail());
            userInfo.setFullName(user.getFullName());
            userInfo.setPhone(user.getPhone() != null ? user.getPhone() : "");
            userInfo.setRole(user.getRole().getName());
            userInfo.setProvider(user.getEmail().contains("gmail") ? "Google" : "Email");
            
            AuthDTO response = new AuthDTO(
                "Login successful with " + (user.getEmail().contains("gmail") ? "Gmail" : "Email"),
                userInfo
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthDTO("Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Register with Firebase token", 
               description = "Register new user with Firebase ID token")
    @ApiResponse(responseCode = "200", description = "Registration successful",
                content = @Content(schema = @Schema(implementation = AuthDTO.class)))
    @ApiResponse(responseCode = "400", description = "Registration failed",
                content = @Content(schema = @Schema(implementation = AuthDTO.class)))
    public ResponseEntity<AuthDTO> register(@RequestBody RegisterDTO request) {
        try {
            String token = request.getToken();
            String phone = request.getPhone();
            
            User user = userService.verifyAndGetUser(token);
            
            if (phone != null && !phone.isEmpty()) {
                user.setPhone(phone);
                user = userService.createOrUpdateUser(
                    user.getFirebaseUid(), 
                    user.getEmail(), 
                    user.getFullName(), 
                    phone
                );
            }
            
            // Create response
            AuthDTO.UserInfo userInfo = new AuthDTO.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setEmail(user.getEmail());
            userInfo.setFullName(user.getFullName());
            userInfo.setPhone(user.getPhone() != null ? user.getPhone() : "");
            userInfo.setRole(user.getRole().getName());
            userInfo.setProvider(user.getEmail().contains("gmail") ? "Google" : "Email");
            
            AuthDTO response = new AuthDTO("Registration successful", userInfo);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthDTO("Registration failed: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user info", 
               description = "Get current authenticated user information")
    @ApiResponse(responseCode = "200", description = "User info retrieved successfully",
                content = @Content(schema = @Schema(implementation = AuthDTO.class)))
    @ApiResponse(responseCode = "400", description = "Failed to get user info",
                content = @Content(schema = @Schema(implementation = AuthDTO.class)))
    public ResponseEntity<AuthDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            User user = userService.verifyAndGetUser(token);
            
            // Create response
            AuthDTO.UserInfo userInfo = new AuthDTO.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setEmail(user.getEmail());
            userInfo.setFullName(user.getFullName());
            userInfo.setPhone(user.getPhone() != null ? user.getPhone() : "");
            userInfo.setRole(user.getRole().getName());
            userInfo.setProvider(user.getEmail().contains("gmail") ? "Google" : "Email");
            
            AuthDTO response = new AuthDTO("User info retrieved successfully", userInfo);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthDTO("Failed to get user info: " + e.getMessage()));
        }
    }
}
