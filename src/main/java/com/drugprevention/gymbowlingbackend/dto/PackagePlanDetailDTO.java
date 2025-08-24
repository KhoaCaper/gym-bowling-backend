package com.drugprevention.gymbowlingbackend.dto;

import java.time.LocalDateTime;

/**
 * DTO cho PackagePlanDetail với thông tin đầy đủ
 * 
 * @author Gym Bowling Team
 * @version 1.0
 */
public class PackagePlanDetailDTO {
    
    private Long id;
    private Long packagePlanId;
    private String packagePlanName;
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private Long centerId;
    private String centerName;
    private Integer sessionsIncluded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public PackagePlanDetailDTO() {}
    
    public PackagePlanDetailDTO(Long id, Long packagePlanId, String packagePlanName, 
                               Long serviceId, String serviceName, String serviceDescription,
                               Long centerId, String centerName, Integer sessionsIncluded,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.packagePlanId = packagePlanId;
        this.packagePlanName = packagePlanName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.centerId = centerId;
        this.centerName = centerName;
        this.sessionsIncluded = sessionsIncluded;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPackagePlanId() { return packagePlanId; }
    public void setPackagePlanId(Long packagePlanId) { this.packagePlanId = packagePlanId; }
    
    public String getPackagePlanName() { return packagePlanName; }
    public void setPackagePlanName(String packagePlanName) { this.packagePlanName = packagePlanName; }
    
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public String getServiceDescription() { return serviceDescription; }
    public void setServiceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; }
    
    public Long getCenterId() { return centerId; }
    public void setCenterId(Long centerId) { this.centerId = centerId; }
    
    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }
    
    public Integer getSessionsIncluded() { return sessionsIncluded; }
    public void setSessionsIncluded(Integer sessionsIncluded) { this.sessionsIncluded = sessionsIncluded; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
