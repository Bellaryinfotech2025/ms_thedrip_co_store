package com.thedripcostore.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.thedripcostore.demo.dto.ProductRequestDto;
import com.thedripcostore.demo.dto.ProductResponseDto;
import com.thedripcostore.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from React
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    // CREATE PRODUCT (DTO + IMAGE)
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> createProduct(
            @ModelAttribute ProductRequestDto dto,
            @RequestParam("image") MultipartFile image) {

        try {
            ProductResponseDto response = service.addProduct(dto, image);
            log.info("Product created successfully: {}", response.getProductId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to create product", e);
            return ResponseEntity.status(500).build();
        }
    }

    // UPDATE IMAGE ONLY
    @PutMapping("/update-image/{productId}")
    public ResponseEntity<ProductResponseDto> updateImage(
            @PathVariable String productId,
            @RequestParam("image") MultipartFile image) {

        try {
            ProductResponseDto response = service.updateImage(productId, image);
            log.info("Product image updated successfully: {}", productId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to update image for productId: {}", productId, e);
            return ResponseEntity.status(500).build();
        }
    }

    // GET IMAGE BY PRODUCT ID
    @GetMapping("/image/{productId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String productId) {
        try {
            byte[] imageData = service.getImageByProductId(productId);
            log.info("Fetched image for productId: {}", productId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageData);
        } catch (Exception e) {
            log.error("Failed to fetch image for productId: {}", productId, e);
            return ResponseEntity.status(404).build();
        }
    }

    // GET ALL PRODUCTS
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        try {
            List<ProductResponseDto> products = service.getAllProducts();
            log.info("Fetched all products, total count: {}", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Failed to fetch all products", e);
            return ResponseEntity.status(500).build();
        }
    }

    // DELETE PRODUCT BY PRODUCT ID
    @DeleteMapping("/deleteByProductId/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        try {
            service.deleteProductByProductId(productId);
            log.info("Product deleted successfully: {}", productId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete product with productId: {}", productId, e);
            return ResponseEntity.status(500).build();
        }
    }
}
