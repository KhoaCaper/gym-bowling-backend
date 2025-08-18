package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.entity.Order;
import com.drugprevention.gymbowlingbackend.entity.Payment;
import com.drugprevention.gymbowlingbackend.repository.PaymentRepository;
import com.drugprevention.gymbowlingbackend.service.OrderService;
import com.drugprevention.gymbowlingbackend.service.UserService;
import com.drugprevention.gymbowlingbackend.service.VNPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final VNPayService vnPayService;
    private final OrderService orderService;
    private final UserService userService;
    private final PaymentRepository paymentRepository;

    public PaymentController(VNPayService vnPayService, 
                           OrderService orderService,
                           UserService userService,
                           PaymentRepository paymentRepository) {
        this.vnPayService = vnPayService;
        this.orderService = orderService;
        this.userService = userService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Long> request, 
                                       Authentication authentication) {
        try {
            String firebaseUid = (String) authentication.getPrincipal();
            Long packagePlanId = request.get("packagePlanId");

            Order order = orderService.createOrder(firebaseUid, packagePlanId);
            
            String orderInfo = "Payment for order #" + order.getId();
            String paymentUrl = vnPayService.createPaymentUrl(
                order.getTotalAmount().longValue(),
                orderInfo,
                order.getId().toString()
            );

            return ResponseEntity.ok(Map.of(
                "orderId", order.getId(),
                "paymentUrl", paymentUrl,
                "message", "Order created successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create order: " + e.getMessage()));
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        try {
            boolean isValidSignature = vnPayService.validateSignature(params);
            
            if (!isValidSignature) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid signature"));
            }

            String orderId = params.get("vnp_TxnRef");
            String responseCode = params.get("vnp_ResponseCode");
            String transactionId = params.get("vnp_TransactionNo");

            Order order = orderService.getOrderById(Long.parseLong(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));

            Payment payment = order.getPayment();
            payment.setTransactionId(transactionId);
            payment.setVnpayResponse(params.toString());

            if ("00".equals(responseCode)) {
                // Payment successful
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                orderService.updateOrderStatus(order.getId(), Order.OrderStatus.PAID);
                
                paymentRepository.save(payment);

                return ResponseEntity.ok(Map.of(
                    "message", "Payment successful",
                    "orderId", orderId,
                    "transactionId", transactionId
                ));
            } else {
                // Payment failed
                payment.setStatus(Payment.PaymentStatus.FAILED);
                orderService.updateOrderStatus(order.getId(), Order.OrderStatus.CANCELLED);
                
                paymentRepository.save(payment);

                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Payment failed",
                    "responseCode", responseCode
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Payment processing failed: " + e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getUserOrders(Authentication authentication) {
        try {
            String firebaseUid = (String) authentication.getPrincipal();
            var orders = orderService.getUserOrders(firebaseUid);
            
            return ResponseEntity.ok(Map.of("orders", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get orders: " + e.getMessage()));
        }
    }
}
