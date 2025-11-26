package com.thedripcostore.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thedripcostore.demo.dto.ProductRequestDto;
import com.thedripcostore.demo.dto.ProductResponseDto;
import com.thedripcostore.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from React
public class ProductController {

    @Autowired
    private ProductService service;

   
    //  CREATE PRODUCT (DTO + IMAGE)
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> createProduct(
            @ModelAttribute ProductRequestDto dto,
            @RequestParam("image") MultipartFile image) {

        return ResponseEntity.ok(service.addProduct(dto, image));
    }

 
    //  UPDATE IMAGE ONLY
    @PutMapping("/update-image/{productId}")
    public ResponseEntity<ProductResponseDto> updateImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image) {

        return ResponseEntity.ok(service.updateImage(productId, image));
    }

    //  GET IMAGE BY PRODUCT ID
    @GetMapping("/image/{productId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String productId) {

        byte[] imageData = service.getImageByProductId(productId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);
    }

    //  GET ALL PRODUCTS
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }
}
