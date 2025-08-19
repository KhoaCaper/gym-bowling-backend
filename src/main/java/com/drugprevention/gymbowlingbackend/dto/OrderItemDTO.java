package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Schema(description = "Order item information")
public class OrderItemDTO {
    
    @NotNull(message = "Package plan ID is required")
    @Schema(description = "Package plan ID", example = "1", required = true)
    private Long packagePlanId;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Schema(description = "Quantity of packages", example = "1", required = true)
    private Integer quantity;
    
    @Schema(description = "Start time for the package", example = "2024-01-01T06:00:00")
    private LocalDateTime startTime;
    
    @Schema(description = "End time for the package", example = "2024-01-01T22:00:00")
    private LocalDateTime endTime;
    
    // Constructors
    public OrderItemDTO() {}
    
    public OrderItemDTO(Long packagePlanId, Integer quantity) {
        this.packagePlanId = packagePlanId;
        this.quantity = quantity;
    }
    
    public OrderItemDTO(Long packagePlanId, Integer quantity, LocalDateTime startTime, LocalDateTime endTime) {
        this.packagePlanId = packagePlanId;
        this.quantity = quantity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters and Setters
    public Long getPackagePlanId() { return packagePlanId; }
    public void setPackagePlanId(Long packagePlanId) { this.packagePlanId = packagePlanId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
