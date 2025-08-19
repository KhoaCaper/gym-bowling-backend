package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.dto.OrderDTO;
import com.drugprevention.gymbowlingbackend.dto.OrderItemDTO;
import com.drugprevention.gymbowlingbackend.entity.Order;
import com.drugprevention.gymbowlingbackend.entity.User;
import com.drugprevention.gymbowlingbackend.entity.PackagePlan;
import com.drugprevention.gymbowlingbackend.entity.OrderPackage;
import com.drugprevention.gymbowlingbackend.repository.OrderRepository;
import com.drugprevention.gymbowlingbackend.repository.UserRepository;
import com.drugprevention.gymbowlingbackend.repository.PackagePlanRepository;
import com.drugprevention.gymbowlingbackend.repository.OrderPackageRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PackagePlanRepository packagePlanRepository;
    private final OrderPackageRepository orderPackageRepository;

    public OrderService(OrderRepository orderRepository, 
                       UserRepository userRepository,
                       PackagePlanRepository packagePlanRepository,
                       OrderPackageRepository orderPackageRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.packagePlanRepository = packagePlanRepository;
        this.orderPackageRepository = orderPackageRepository;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findByOrderByOrderDateDesc()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByStatus(orderStatus)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }

    public Optional<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    public Optional<Order> getOrderEntityById(Long id) {
        return orderRepository.findById(id);
    }

    public OrderDTO createOrder(Long userId, List<OrderItemDTO> orderItems) {
        // Validate user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (orderItems == null || orderItems.isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        // Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO item : orderItems) {
            PackagePlan packagePlan = packagePlanRepository.findById(item.getPackagePlanId())
                .orElseThrow(() -> new RuntimeException("Package plan not found with id: " + item.getPackagePlanId()));
            
            if (!packagePlan.getIsActive()) {
                throw new RuntimeException("Package plan is not active: " + packagePlan.getName());
            }
            
            totalAmount = totalAmount.add(packagePlan.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // Create order
        Order order = new Order(user, totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Create order packages
        for (OrderItemDTO item : orderItems) {
            PackagePlan packagePlan = packagePlanRepository.findById(item.getPackagePlanId()).get();
            
            OrderPackage orderPackage = new OrderPackage(
                savedOrder,
                packagePlan,
                item.getQuantity(),
                packagePlan.getPrice()
            );
            
            if (item.getStartTime() != null) {
                orderPackage.setStartTime(item.getStartTime());
            }
            if (item.getEndTime() != null) {
                orderPackage.setEndTime(item.getEndTime());
            }
            
            orderPackageRepository.save(orderPackage);
        }

        return convertToDTO(savedOrder);
    }

    public OrderDTO updateOrderStatus(Long id, String status) {
        return orderRepository.findById(id)
            .map(order -> {
                try {
                    Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
                    order.setStatus(orderStatus);
                    Order savedOrder = orderRepository.save(order);
                    return convertToDTO(savedOrder);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid order status: " + status);
                }
            })
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO(
            order.getId(),
            order.getUser().getId(),
            order.getUser().getEmail(),
            order.getUser().getFullName(),
            order.getTotalAmount(),
            order.getStatus().toString(),
            order.getOrderDate()
        );

        // Set payment info if exists
        if (order.getPayment() != null) {
            // TODO: Convert payment to DTO
        }

        // Set order packages if exists
        if (order.getOrderPackages() != null && !order.getOrderPackages().isEmpty()) {
            List<com.drugprevention.gymbowlingbackend.dto.OrderPackageDTO> orderPackageDTOs = 
                order.getOrderPackages().stream()
                    .map(this::convertOrderPackageToDTO)
                    .collect(Collectors.toList());
            orderDTO.setOrderPackages(orderPackageDTOs);
        }

        return orderDTO;
    }

    private com.drugprevention.gymbowlingbackend.dto.OrderPackageDTO convertOrderPackageToDTO(OrderPackage orderPackage) {
        return new com.drugprevention.gymbowlingbackend.dto.OrderPackageDTO(
            orderPackage.getId(),
            orderPackage.getOrder().getId(),
            orderPackage.getPackagePlan().getId(),
            orderPackage.getPackagePlan().getName(),
            orderPackage.getQuantity(),
            orderPackage.getUnitPrice(),
            orderPackage.getSubtotal(),
            orderPackage.getStartTime(),
            orderPackage.getEndTime(),
            orderPackage.getCreatedAt()
        );
    }


}
