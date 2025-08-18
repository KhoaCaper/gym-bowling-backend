package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response")
public class AuthDTO {
    
    @Schema(description = "Success or error message", 
            example = "Login successful with Gmail")
    private String message;
    
    @Schema(description = "User information")
    private UserInfo user;
    
    @Schema(description = "Error message (only present on failure)", 
            example = "Login failed: Invalid token")
    private String error;
    
    // Constructors
    public AuthDTO() {}
    
    public AuthDTO(String message, UserInfo user) {
        this.message = message;
        this.user = user;
    }
    
    public AuthDTO(String error) {
        this.error = error;
    }
    
    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    // Inner class for user info
    @Schema(description = "User information")
    public static class UserInfo {
        @Schema(description = "User ID", example = "1")
        private Long id;
        
        @Schema(description = "Email address", example = "john@gmail.com")
        private String email;
        
        @Schema(description = "Full name", example = "John Doe")
        private String fullName;
        
        @Schema(description = "Phone number", example = "0901234567")
        private String phone;
        
        @Schema(description = "User role", example = "USER")
        private String role;
        
        @Schema(description = "Authentication provider", example = "Google")
        private String provider;
        
        // Constructors
        public UserInfo() {}
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
    }
}