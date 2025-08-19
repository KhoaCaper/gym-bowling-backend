package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Order package information")
public class OrderPackageDTO {
    
    @Schema(description = "Order package ID", example = "1")
    private Long id;
    
    @Schema(description = "Order ID", example = "1")
    private Long orderId;
    
    @Schema(description = "Package plan ID", example = "1")
    private Long packagePlanId;
    
    @Schema(description = "Package plan name", example = "Gói Cơ Bản")
    private String packagePlanName;
    
    @Schema(description = "Quantity", example = "1")
    private Integer quantity;
    
    @Schema(description = "Unit price", example = "1500000.00")
    private BigDecimal unitPrice;
    
    @Schema(description = "Subtotal", example = "1500000.00")
    private BigDecimal subtotal;
    
    @Schema(description = "Start time")
    private LocalDateTime startTime;
    
    @Schema(description = "End time")
    private LocalDateTime endTime;
    
    @Schema(description = "Creation date")
    private LocalDateTime createdAt;
    
    // Constructors
    public OrderPackageDTO() {}
    
    public OrderPackageDTO(Long id, Long orderId, Long packagePlanId, String packagePlanName, 
                          Integer quantity, BigDecimal unitPrice, BigDecimal subtotal, 
                          LocalDateTime startTime, LocalDateTime endTime, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.packagePlanId = packagePlanId;
        this.packagePlanName = packagePlanName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public Long getPackagePlanId() { return packagePlanId; }
    public void setPackagePlanId(Long packagePlanId) { this.packagePlanId = packagePlanId; }
    
    public String getPackagePlanName() { return packagePlanName; }
    public void setPackagePlanName(String packagePlanName) { this.packagePlanName = packagePlanName; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
