package com.drugprevention.gymbowlingbackend.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO để tạo gói dịch vụ hoàn chỉnh với services và timeframes
 * 
 * @author Gym Bowling Team
 * @version 1.0
 */
public class CreateCompletePackageDTO {
    
    // Thông tin gói dịch vụ cơ bản
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMonths;
    private Long centerId;
    
    // Danh sách services và timeframes
    private List<ServiceTimeFrameDTO> services;
    
    // Constructors
    public CreateCompletePackageDTO() {}
    
    public CreateCompletePackageDTO(String name, String description, BigDecimal price, 
                                  Integer durationMonths, Long centerId, List<ServiceTimeFrameDTO> services) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMonths = durationMonths;
        this.centerId = centerId;
        this.services = services;
    }
    
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
    
    public List<ServiceTimeFrameDTO> getServices() { return services; }
    public void setServices(List<ServiceTimeFrameDTO> services) { this.services = services; }
    
    /**
     * DTO con cho service và timeframe
     */
    public static class ServiceTimeFrameDTO {
        private Long serviceId;
        private Long timeFrameId;
        private Integer sessionsIncluded;
        
        // Constructors
        public ServiceTimeFrameDTO() {}
        
        public ServiceTimeFrameDTO(Long serviceId, Long timeFrameId, Integer sessionsIncluded) {
            this.serviceId = serviceId;
            this.timeFrameId = timeFrameId;
            this.sessionsIncluded = sessionsIncluded;
        }
        
        // Getters and Setters
        public Long getServiceId() { return serviceId; }
        public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
        
        public Long getTimeFrameId() { return timeFrameId; }
        public void setTimeFrameId(Long timeFrameId) { this.timeFrameId = timeFrameId; }
        
        public Integer getSessionsIncluded() { return sessionsIncluded; }
        public void setSessionsIncluded(Integer sessionsIncluded) { this.sessionsIncluded = sessionsIncluded; }
    }
}
