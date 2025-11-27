package com.thedripcostore.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thedripcostore.demo.entity.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserUserId(String userId);
    
    Optional<UserAddress> findByAddressId(String addressId); // get single address by addressId
}
