package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Schema(description = "Time frame information")
public class TimeFrameDTO {
    
    @Schema(description = "Time frame ID", example = "1")
    private Long id;
    
    @Schema(description = "Center ID", example = "1")
    private Long centerId;
    
    @Schema(description = "Center name", example = "GymBo Center 1")
    private String centerName;
    
    @Schema(description = "Start time", example = "06:00:00")
    private LocalTime startTime;
    
    @Schema(description = "End time", example = "22:00:00")
    private LocalTime endTime;
    
    @Schema(description = "Day of week", example = "MONDAY")
    private String dayOfWeek;
    
    @Schema(description = "Is time frame available", example = "true")
    private Boolean isAvailable;
    
    @Schema(description = "Time frame creation date")
    private LocalDateTime createdAt;
    
    // Constructors
    public TimeFrameDTO() {}
    
    public TimeFrameDTO(Long id, Long centerId, String centerName, LocalTime startTime, 
                       LocalTime endTime, String dayOfWeek, Boolean isAvailable, LocalDateTime createdAt) {
        this.id = id;
        this.centerId = centerId;
        this.centerName = centerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCenterId() { return centerId; }
    public void setCenterId(Long centerId) { this.centerId = centerId; }
    
    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
