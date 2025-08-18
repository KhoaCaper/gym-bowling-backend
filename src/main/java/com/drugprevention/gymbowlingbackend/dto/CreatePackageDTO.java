package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

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
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
