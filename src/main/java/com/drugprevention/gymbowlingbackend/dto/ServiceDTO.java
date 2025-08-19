package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Service information")
public class ServiceDTO {
    
    @Schema(description = "Service ID", example = "1")
    private Long id;
    
    @Schema(description = "Service type ID", example = "1")
    private Long serviceTypeId;
    
    @Schema(description = "Service type name", example = "GYM")
    private String serviceTypeName;
    
    @Schema(description = "Center ID", example = "1")
    private Long centerId;
    
    @Schema(description = "Center name", example = "GymBo Center 1")
    private String centerName;
    
    @Schema(description = "Service name", example = "Gym Training")
    private String name;
    
    @Schema(description = "Service description", example = "Tập gym với đầy đủ thiết bị hiện đại")
    private String description;
    
    @Schema(description = "Service price", example = "100000.00")
    private BigDecimal price;
    
    @Schema(description = "Is service active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Service creation date")
    private LocalDateTime createdAt;
    
    // Constructors
    public ServiceDTO() {}
    
    public ServiceDTO(Long id, Long serviceTypeId, String serviceTypeName, Long centerId, 
                     String centerName, String name, String description, BigDecimal price, 
                     Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeName = serviceTypeName;
        this.centerId = centerId;
        this.centerName = centerName;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(Long serviceTypeId) { this.serviceTypeId = serviceTypeId; }
    
    public String getServiceTypeName() { return serviceTypeName; }
    public void setServiceTypeName(String serviceTypeName) { this.serviceTypeName = serviceTypeName; }
    
    public Long getCenterId() { return centerId; }
    public void setCenterId(Long centerId) { this.centerId = centerId; }
    
    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
