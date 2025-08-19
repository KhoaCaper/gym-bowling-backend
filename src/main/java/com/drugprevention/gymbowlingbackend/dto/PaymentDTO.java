package com.drugprevention.gymbowlingbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Payment information")
public class PaymentDTO {
    
    @Schema(description = "Payment ID", example = "1")
    private Long id;
    
    @Schema(description = "Order ID", example = "1")
    private Long orderId;
    
    @Schema(description = "Payment amount", example = "1500000.00")
    private BigDecimal amount;
    
    @Schema(description = "Payment method", example = "VNPAY")
    private String paymentMethod;
    
    @Schema(description = "Transaction ID", example = "VNPAY123456789")
    private String transactionId;
    
    @Schema(description = "Payment status", example = "SUCCESS")
    private String status;
    
    @Schema(description = "Payment date")
    private LocalDateTime paymentDate;
    
    @Schema(description = "VNPay response data")
    private String vnpayResponse;
    
    // Constructors
    public PaymentDTO() {}
    
    public PaymentDTO(Long id, Long orderId, BigDecimal amount, String paymentMethod, 
                     String transactionId, String status, LocalDateTime paymentDate) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
        this.paymentDate = paymentDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public String getVnpayResponse() { return vnpayResponse; }
    public void setVnpayResponse(String vnpayResponse) { this.vnpayResponse = vnpayResponse; }
}
