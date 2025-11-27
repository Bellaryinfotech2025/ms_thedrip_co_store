package com.thedripcostore.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thedripcostore.demo.dto.OrderRequestDto;
import com.thedripcostore.demo.entity.OrderEntity;
import com.thedripcostore.demo.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) { 
        this.orderService = orderService; 
    }

    @PostMapping
    public ResponseEntity<OrderEntity> placeOrder(@RequestBody OrderRequestDto dto) {
        try {
            OrderEntity order = orderService.placeOrder(dto);
            log.info("Order placed successfully: {}", order.getOrderId());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            log.error("Failed to place order for userId: {}", dto.getUserId(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable String orderId) {
        try {
            OrderEntity order = orderService.getOrderByOrderId(orderId);
            log.info("Fetched order successfully: {}", orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            log.error("Failed to fetch order: {}", orderId, e);
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/get/allorders")
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        try {
            List<OrderEntity> orders = orderService.getAllOrders();
            log.info("Fetched all orders, total count: {}", orders.size());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Failed to fetch all orders", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getOrdersByUserId/{userId}")
    public ResponseEntity<List<OrderEntity>> getOrdersByUserId(@PathVariable String userId) {
        try {
            List<OrderEntity> orders = orderService.getOrdersByUserId(userId);
            log.info("Fetched {} orders for userId: {}", orders.size(), userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Failed to fetch orders for userId: {}", userId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getOrdersByAddressId/{addressId}")
    public ResponseEntity<List<OrderEntity>> getOrdersByAddressId(@PathVariable String addressId) {
        try {
            List<OrderEntity> orders = orderService.getOrdersByAddressId(addressId);
            log.info("Fetched {} orders for addressId: {}", orders.size(), addressId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Failed to fetch orders for addressId: {}", addressId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/user/{userId}/status")
    public ResponseEntity<OrderEntity> updateOrderStatusByUserId(
            @PathVariable String userId,
            @RequestParam String status) {
        try {
            OrderEntity order = orderService.updateOrderStatusByUserId(userId, status);
            log.info("Updated order status for userId: {} to '{}'", userId, status);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            log.error("Failed to update order status for userId: {}", userId, e);
            return ResponseEntity.status(500).build();
        }
    }
}
