package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Request body for creating a new service")
public class CreateServiceDTO {
    
    @NotNull(message = "Service type ID is required")
    @Schema(description = "Service type ID", example = "1", required = true)
    private Long serviceTypeId;
    
    @NotNull(message = "Center ID is required")
    @Schema(description = "Center ID", example = "1", required = true)
    private Long centerId;
    
    @NotBlank(message = "Service name is required")
    @Size(min = 2, max = 100, message = "Service name must be between 2 and 100 characters")
    @Schema(description = "Service name", example = "Gym Training", required = true)
    private String name;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Schema(description = "Service description", example = "Tập gym với đầy đủ thiết bị hiện đại")
    private String description;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Schema(description = "Service price", example = "100000.00", required = true)
    private BigDecimal price;
    
    // Constructors
    public CreateServiceDTO() {}
    
    public CreateServiceDTO(Long serviceTypeId, Long centerId, String name, String description, BigDecimal price) {
        this.serviceTypeId = serviceTypeId;
        this.centerId = centerId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Getters and Setters
    public Long getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(Long serviceTypeId) { this.serviceTypeId = serviceTypeId; }
    
    public Long getCenterId() { return centerId; }
    public void setCenterId(Long centerId) { this.centerId = centerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
