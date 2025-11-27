package com.thedripcostore.demo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thedripcostore.demo.dto.OrderRequestDto;
import com.thedripcostore.demo.entity.OrderEntity;
import com.thedripcostore.demo.repository.OrderRepository;
import com.thedripcostore.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepo;

    public OrderServiceImpl(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    @Transactional
    public OrderEntity placeOrder(OrderRequestDto dto) {
        try {
            OrderEntity order = new OrderEntity();
            order.setUserId(dto.getUserId());
            order.setAddressId(dto.getAddressId());
            order.setProductId(dto.getProductId());
            order.setProductTitle(dto.getProductTitle());
            order.setProductCategory(dto.getProductCategory());
            order.setProductSize(dto.getProductSize());
            order.setUnitPrice(dto.getUnitPrice());
            order.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 1);
            order.calculateSubtotal();
            order.setStatus("PLACED");

            OrderEntity saved = orderRepo.save(order);
            log.info("Order placed successfully: {}", saved.getOrderId());
            return saved;

        } catch (Exception e) {
            log.error("Failed to place order for userId: {}", dto.getUserId(), e);
            throw new RuntimeException("Failed to place order", e);
        }
    }

    @Override
    public OrderEntity getOrderByOrderId(String orderId) {
        try {
            OrderEntity order = orderRepo.findByOrderId(orderId)
                    .orElseThrow(() -> {
                        log.warn("Order not found: {}", orderId);
                        return new RuntimeException("Order not found: " + orderId);
                    });
            log.info("Fetched order successfully: {}", orderId);
            return order;
        } catch (Exception e) {
            log.error("Failed to fetch order: {}", orderId, e);
            throw new RuntimeException("Failed to fetch order", e);
        }
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        try {
            List<OrderEntity> orders = orderRepo.findAll();
            log.info("Fetched all orders, total count: {}", orders.size());
            return orders;
        } catch (Exception e) {
            log.error("Failed to fetch all orders", e);
            throw new RuntimeException("Failed to fetch orders", e);
        }
    }

    @Override
    public List<OrderEntity> getOrdersByUserId(String userId) {
        try {
            List<OrderEntity> orders = orderRepo.findByUserId(userId);
            log.info("Fetched {} orders for userId: {}", orders.size(), userId);
            return orders;
        } catch (Exception e) {
            log.error("Failed to fetch orders for userId: {}", userId, e);
            throw new RuntimeException("Failed to fetch orders", e);
        }
    }

    @Override
    public List<OrderEntity> getOrdersByAddressId(String addressId) {
        try {
            List<OrderEntity> orders = orderRepo.findByAddressId(addressId);
            log.info("Fetched {} orders for addressId: {}", orders.size(), addressId);
            return orders;
        } catch (Exception e) {
            log.error("Failed to fetch orders for addressId: {}", addressId, e);
            throw new RuntimeException("Failed to fetch orders", e);
        }
    }

    @Override
    @Transactional
    public OrderEntity updateOrderStatusByUserId(String userId, String newStatus) {
        try {
            List<OrderEntity> orders = orderRepo.findByUserId(userId);
            if (orders.isEmpty()) {
                log.warn("No orders found for userId: {}", userId);
                throw new RuntimeException("No orders found for userId: " + userId);
            }

            orders.forEach(o -> o.setStatus(newStatus));
            orderRepo.saveAll(orders);

            log.info("Updated status for {} orders of userId: {} to '{}'", orders.size(), userId, newStatus);
            return orders.get(0); // return first updated order as example

        } catch (Exception e) {
            log.error("Failed to update order status for userId: {}", userId, e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }
}
