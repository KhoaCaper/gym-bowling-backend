package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request with Firebase token")
public class LoginDTO {
    
    @NotBlank(message = "Token is required")
    @Schema(description = "Firebase ID token from frontend", 
            example = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjE2NzAyN...")
    private String token;
    
    @Schema(description = "User's phone number (optional)", 
            example = "0901234567")
    private String phone;
    
    // Constructors
    public LoginDTO() {}
    
    public LoginDTO(String token, String phone) {
        this.token = token;
        this.phone = phone;
    }
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
