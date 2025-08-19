package com.drugprevention.gymbowlingbackend.controller;

import com.drugprevention.gymbowlingbackend.dto.OrderDTO;
import com.drugprevention.gymbowlingbackend.dto.OrderItemDTO;
import com.drugprevention.gymbowlingbackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get all orders", 
               description = "Get all orders sorted by order date")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
                content = @Content(schema = @Schema(implementation = OrderDTO.class)))
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user", 
               description = "Get all orders for a specific user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", 
               description = "Get all orders with a specific status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved orders by status")
    @ApiResponse(responseCode = "400", description = "Invalid status")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<OrderDTO> orders = orderService.getOrdersByStatus(status);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(List.of()); // Return empty list for invalid status
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", 
               description = "Get a specific order by its ID")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
            .map(order -> ResponseEntity.ok(order))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new order", 
               description = "Create a new order with package items")
    @ApiResponse(responseCode = "200", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or validation failed")
    public ResponseEntity<?> createOrder(
            @RequestParam Long userId,
            @RequestBody List<OrderItemDTO> orderItems) {
        try {
            OrderDTO createdOrder = orderService.createOrder(userId, orderItems);
            return ResponseEntity.ok(Map.of(
                "message", "Order created successfully",
                "order", createdOrder
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create order: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status", 
               description = "Update the status of an existing order")
    @ApiResponse(responseCode = "200", description = "Order status updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid status or order not found")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        try {
            OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(Map.of(
                "message", "Order status updated successfully",
                "order", updatedOrder
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update order status: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", 
               description = "Delete an order by ID")
    @ApiResponse(responseCode = "200", description = "Order deleted successfully")
    @ApiResponse(responseCode = "400", description = "Order not found")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok(Map.of("message", "Order deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete order: " + e.getMessage()));
        }
    }
}
