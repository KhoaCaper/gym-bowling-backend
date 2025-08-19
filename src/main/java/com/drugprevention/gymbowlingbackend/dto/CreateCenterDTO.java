package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Schema(description = "Request body for creating a new center")
public class CreateCenterDTO {
    
    @NotBlank(message = "Center name is required")
    @Size(min = 2, max = 100, message = "Center name must be between 2 and 100 characters")
    @Schema(description = "Center name", example = "GymBo Center 1", required = true)
    private String name;
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    @Schema(description = "Center address", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Schema(description = "Center phone", example = "0901234567")
    private String phone;
    
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(description = "Center email", example = "center@gymbo.com")
    private String email;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Schema(description = "Center description", example = "Trung tâm gym và bowling hiện đại")
    private String description;
    
    // Constructors
    public CreateCenterDTO() {}
    
    public CreateCenterDTO(String name, String address, String phone, String email, String description) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
    }
    
    // Getters and Setters
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
}
