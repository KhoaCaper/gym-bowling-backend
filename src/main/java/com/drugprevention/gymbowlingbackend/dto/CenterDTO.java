package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Center information")
public class CenterDTO {
    
    @Schema(description = "Center ID", example = "1")
    private Long id;
    
    @Schema(description = "Center name", example = "GymBo Center 1")
    private String name;
    
    @Schema(description = "Center address", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;
    
    @Schema(description = "Center phone", example = "0901234567")
    private String phone;
    
    @Schema(description = "Center email", example = "center@gymbo.com")
    private String email;
    
    @Schema(description = "Center description", example = "Trung tâm gym và bowling hiện đại")
    private String description;
    
    @Schema(description = "Is center active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Center creation date")
    private LocalDateTime createdAt;
    
    // Constructors
    public CenterDTO() {}
    
    public CenterDTO(Long id, String name, String address, String phone, String email, String description, 
                    Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
