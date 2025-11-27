package com.thedripcostore.demo.service;

import java.util.List;

import com.thedripcostore.demo.dto.OrderRequestDto;
import com.thedripcostore.demo.entity.OrderEntity;

public interface OrderService {
    OrderEntity placeOrder(OrderRequestDto dto);
    OrderEntity getOrderByOrderId(String orderId);
    
    List<OrderEntity> getAllOrders();

    List<OrderEntity> getOrdersByUserId(String userId);

    List<OrderEntity> getOrdersByAddressId(String addressId);

    OrderEntity updateOrderStatusByUserId(String userId, String newStatus);
}
