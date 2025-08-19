package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Order information")
public class OrderDTO {
    
    @Schema(description = "Order ID", example = "1")
    private Long id;
    
    @Schema(description = "User ID", example = "1")
    private Long userId;
    
    @Schema(description = "User email", example = "user@example.com")
    private String userEmail;
    
    @Schema(description = "User full name", example = "John Doe")
    private String userFullName;
    
    @Schema(description = "Total amount", example = "1500000.00")
    private BigDecimal totalAmount;
    
    @Schema(description = "Order status", example = "PENDING")
    private String status;
    
    @Schema(description = "Order date")
    private LocalDateTime orderDate;
    
    @Schema(description = "Payment information")
    private PaymentDTO payment;
    
    @Schema(description = "Order packages")
    private List<OrderPackageDTO> orderPackages;
    
    // Constructors
    public OrderDTO() {}
    
    public OrderDTO(Long id, Long userId, String userEmail, String userFullName, 
                   BigDecimal totalAmount, String status, LocalDateTime orderDate) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public PaymentDTO getPayment() { return payment; }
    public void setPayment(PaymentDTO payment) { this.payment = payment; }
    
    public List<OrderPackageDTO> getOrderPackages() { return orderPackages; }
    public void setOrderPackages(List<OrderPackageDTO> orderPackages) { this.orderPackages = orderPackages; }
}
