package com.drugprevention.gymbowlingbackend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Remove direct relationship to avoid schema conflicts
    // Use OrderPackage for package relationships instead
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();
    
    @Column
    private LocalDateTime updatedAt;
    
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
    
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderPackage> orderPackages;
    
    public enum OrderStatus {
        PENDING, PAID, CANCELLED, COMPLETED
    }
    
    // Constructors
    public Order() {}
    
    public Order(User user, BigDecimal totalAmount) {
        this.user = user;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    // PackagePlan relationship removed - use OrderPackage instead
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
    
    public List<OrderPackage> getOrderPackages() { return orderPackages; }
    public void setOrderPackages(List<OrderPackage> orderPackages) { this.orderPackages = orderPackages; }
}
