package com.thedripcostore.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thedripcostore.demo.entity.ProductDetails;

public interface ProductRepository extends JpaRepository<ProductDetails, Long> {
    Optional<ProductDetails> findByProductId(String productId);
    void deleteByProductId(String productId);
    Optional<ProductDetails> findByImageName(String imagePath);

}
