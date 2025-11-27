package com.thedripcostore.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thedripcostore.demo.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderId(String orderId);
    
    List<OrderEntity> findByUserId(String userId);

    List<OrderEntity> findByAddressId(String addressId);
}
