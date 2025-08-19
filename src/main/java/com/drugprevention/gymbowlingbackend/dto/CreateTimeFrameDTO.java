package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body for creating a new time frame")
public class CreateTimeFrameDTO {
    
    @NotNull(message = "Center ID is required")
    @Schema(description = "Center ID", example = "1", required = true)
    private Long centerId;
    
    @NotBlank(message = "Day of week is required")
    @Pattern(regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)$", 
             message = "Day of week must be one of: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY")
    @Schema(description = "Day of week", example = "MONDAY", required = true)
    private String dayOfWeek;
    
    @NotBlank(message = "Start time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$", 
             message = "Start time must be in format HH:MM:SS")
    @Schema(description = "Start time in format HH:MM:SS", example = "06:00:00", required = true)
    private String startTime;
    
    @NotBlank(message = "End time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$", 
             message = "End time must be in format HH:MM:SS")
    @Schema(description = "End time in format HH:MM:SS", example = "22:00:00", required = true)
    private String endTime;
    
    // Constructors
    public CreateTimeFrameDTO() {}
    
    public CreateTimeFrameDTO(Long centerId, String dayOfWeek, String startTime, String endTime) {
        this.centerId = centerId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters and Setters
    public Long getCenterId() { return centerId; }
    public void setCenterId(Long centerId) { this.centerId = centerId; }
    
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
