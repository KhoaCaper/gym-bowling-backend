package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.entity.Order;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.entity.Payment;
import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.repository.OrderRepository;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanRepository;
import com.drugprevention.gymbowlingbackend.repository.PaymentRepository;
import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PackagePlanRepository packagePlanRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository, 
                       UserRepository userRepository,
                       PackagePlanRepository packagePlanRepository,
                       PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.packagePlanRepository = packagePlanRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Order createOrder(String firebaseUid, Long packagePlanId) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
            .orElseThrow(() -> new RuntimeException("User not found"));

        PackagePlan packagePlan = packagePlanRepository.findById(packagePlanId)
            .orElseThrow(() -> new RuntimeException("Package plan not found"));

        if (!packagePlan.getIsActive()) {
            throw new RuntimeException("Package plan is not active");
        }

        Order order = new Order(user, packagePlan.getPrice());
        order = orderRepository.save(order);

        // Create payment record
        Payment payment = new Payment(order, packagePlan.getPrice(), Payment.PaymentMethod.VNPAY);
        paymentRepository.save(payment);

        return order;
    }

    public List<Order> getUserOrders(String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
