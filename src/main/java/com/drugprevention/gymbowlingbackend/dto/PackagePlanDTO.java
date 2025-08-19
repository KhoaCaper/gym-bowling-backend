package com.drugprevention.gymbowlingbackend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PackagePlanDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMonths;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long centerId;
    private String centerName;
    
    // Constructors
    public PackagePlanDTO() {}
    
    public PackagePlanDTO(Long id, String name, String description, BigDecimal price, 
                         Integer durationMonths, Boolean isActive, LocalDateTime createdAt,
                         Long centerId, String centerName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMonths = durationMonths;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.centerId = centerId;
        this.centerName = centerName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Long getCenterId() { return centerId; }
    public void setCenterId(Long centerId) { this.centerId = centerId; }
    
    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }
}
