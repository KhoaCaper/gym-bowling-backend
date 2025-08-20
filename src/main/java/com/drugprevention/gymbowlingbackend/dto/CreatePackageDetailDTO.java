package com.drugprevention.gymbowlingbackend.dto;

import java.math.BigDecimal;

public class CreatePackageDetailDTO {
    private Long packagePlanId;
    private Long serviceId;
    private Integer sessionsIncluded;
    private BigDecimal price;

    // Constructors
    public CreatePackageDetailDTO() {}

    public CreatePackageDetailDTO(Long packagePlanId, Long serviceId, Integer sessionsIncluded, BigDecimal price) {
        this.packagePlanId = packagePlanId;
        this.serviceId = serviceId;
        this.sessionsIncluded = sessionsIncluded;
        this.price = price;
    }

    // Getters and Setters
    public Long getPackagePlanId() { return packagePlanId; }
    public void setPackagePlanId(Long packagePlanId) { this.packagePlanId = packagePlanId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public Integer getSessionsIncluded() { return sessionsIncluded; }
    public void setSessionsIncluded(Integer sessionsIncluded) { this.sessionsIncluded = sessionsIncluded; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
