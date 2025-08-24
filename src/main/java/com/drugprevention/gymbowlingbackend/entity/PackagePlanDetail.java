package com.drugprevention.gymbowlingbackend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "package_plan_details")
public class PackagePlanDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_plan_id", nullable = false)
    private PackagePlan packagePlan;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @Column(nullable = false)
    private Integer sessionsIncluded = 0;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public PackagePlanDetail() {}
    
    public PackagePlanDetail(PackagePlan packagePlan, Service service, Integer sessionsIncluded) {
        this.packagePlan = packagePlan;
        this.service = service;
        this.sessionsIncluded = sessionsIncluded;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public PackagePlan getPackagePlan() { return packagePlan; }
    public void setPackagePlan(PackagePlan packagePlan) { this.packagePlan = packagePlan; }
    
    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }
    
    public Integer getSessionsIncluded() { return sessionsIncluded; }
    public void setSessionsIncluded(Integer sessionsIncluded) { this.sessionsIncluded = sessionsIncluded; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
