package com.drugprevention.gymbowlingbackend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "package_plans")
public class PackagePlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer durationMonths;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @JsonIgnore
    @OneToMany(mappedBy = "packagePlan", cascade = CascadeType.ALL)
    private List<PackagePlanDetail> packagePlanDetails;
    
    @JsonIgnore
    @OneToMany(mappedBy = "packagePlan", cascade = CascadeType.ALL)
    private List<OrderPackage> orderPackages;
    
    // Constructors
    public PackagePlan() {}
    
    public PackagePlan(String name, String description, BigDecimal price, Integer durationMonths) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMonths = durationMonths;
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
    
    public List<PackagePlanDetail> getPackagePlanDetails() { return packagePlanDetails; }
    public void setPackagePlanDetails(List<PackagePlanDetail> packagePlanDetails) { this.packagePlanDetails = packagePlanDetails; }
    
    public List<OrderPackage> getOrderPackages() { return orderPackages; }
    public void setOrderPackages(List<OrderPackage> orderPackages) { this.orderPackages = orderPackages; }
}
