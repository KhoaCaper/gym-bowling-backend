package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Request body for creating a new package")
public class CreatePackageDTO {
    
    @Schema(description = "Package name", example = "Basic Gym Package", required = true)
    private String name;
    
    @Schema(description = "Package description", example = "Access to gym facilities for 1 month")
    private String description;
    
    @Schema(description = "Package price", example = "1500000.00", required = true)
    private BigDecimal price;
    
    @Schema(description = "Duration in months", example = "1", required = true)
    private Integer durationMonths;
    
    @Schema(description = "Center ID where the package is available", example = "1", required = true)
    private Long centerId;
    
    @Schema(description = "List of service IDs included in this package", example = "[1, 2, 3]")
    private List<Long> serviceIds;
    
    @Schema(description = "Is package active", example = "true")
    private Boolean isActive = true;
    
    // Constructors
    public CreatePackageDTO() {}
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }
    
    public Long getCenterId() { return centerId; }
    public void setCenterId(Long centerId) { this.centerId = centerId; }
    
    public List<Long> getServiceIds() { return serviceIds; }
    public void setServiceIds(List<Long> serviceIds) { this.serviceIds = serviceIds; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
