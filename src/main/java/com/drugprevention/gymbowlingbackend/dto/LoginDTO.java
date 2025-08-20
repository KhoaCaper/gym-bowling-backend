package com.drugprevention.gymbowlingbackend.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login Request DTO")
public class LoginDTO {
    
    @Schema(description = "Username for traditional login", example = "admin")
    private String username;
    
    @Schema(description = "Password for traditional login", example = "admin_password")
    private String password;
    
    @Schema(description = "Firebase UID for Firebase authentication", example = "firebase_uid_123")
    private String firebaseUid;
    
    @Schema(description = "Login method: 'TRADITIONAL' or 'FIREBASE'", example = "TRADITIONAL")
    @NotBlank(message = "Login method is required")
    private String loginMethod;
    
    // Constructors
    public LoginDTO() {}
    
    public LoginDTO(String username, String password, String firebaseUid, String loginMethod) {
        this.username = username;
        this.password = password;
        this.firebaseUid = firebaseUid;
        this.loginMethod = loginMethod;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }
    
    public String getLoginMethod() { return loginMethod; }
    public void setLoginMethod(String loginMethod) { this.loginMethod = loginMethod; }
}
