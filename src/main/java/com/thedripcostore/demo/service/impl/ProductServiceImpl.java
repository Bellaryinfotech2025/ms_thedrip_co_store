package com.thedripcostore.demo.service.impl;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thedripcostore.demo.components.ProductIdGenerator;
import com.thedripcostore.demo.dto.ProductMapper;
import com.thedripcostore.demo.dto.ProductRequestDto;
import com.thedripcostore.demo.dto.ProductResponseDto;
import com.thedripcostore.demo.entity.ProductDetails;
import com.thedripcostore.demo.repository.ProductRepository;
import com.thedripcostore.demo.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String IMAGE_BASE_PATH = "C:/Users/Admin/Desktop/Bellary Info Tech/bellaryinfotech_invoice_files/";
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository repo;

    @Autowired
    private ProductMapper mapper;

    @Override
    @Transactional
    public ProductResponseDto addProduct(ProductRequestDto dto, MultipartFile image) {
        try {
            if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
                log.warn("Product title is missing");
                throw new IllegalArgumentException("Title required");
            }

            if (dto.getPrice() == null) {
                log.warn("Product price is missing");
                throw new IllegalArgumentException("Price required");
            }

            if (dto.getPreviousPrice() == null) {
                log.warn("Product previous price is missing");
                throw new IllegalArgumentException("Previous price required");
            }

            if (image == null || image.isEmpty()) {
                log.warn("Product image is missing");
                throw new IllegalArgumentException("Image required");
            }

            File folder = new File(IMAGE_BASE_PATH);
            if (!folder.exists()) folder.mkdirs();

            ProductDetails p = new ProductDetails();
            String productId = ProductIdGenerator.generate();
            p.setProductId(productId);
            p.setTitle(dto.getTitle());
            p.setCategory(dto.getCategory());
            p.setPrice(dto.getPrice());
            p.setPreviousPrice(dto.getPreviousPrice());
            p.setStock(dto.getStock() != null ? dto.getStock() : 0);
            p.setDescription(dto.getDescription());
            p.setSize(dto.getSize());
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());

            String originalName = Objects.requireNonNull(image.getOriginalFilename())
                    .replaceAll("\\s+", "_");
            String finalImageName = productId + "_" + System.currentTimeMillis() + "_" + originalName;
            File dest = new File(IMAGE_BASE_PATH + finalImageName);
            image.transferTo(dest);

            p.setImageName(finalImageName);
            p.setImagePath(IMAGE_BASE_PATH + finalImageName);
            p.setImageUrl("/api/files/view/image/" + finalImageName);

            ProductDetails saved = repo.save(p);
            log.info("Product added successfully with productId: {}", productId);
            return mapper.toDto(saved);

        } catch (IllegalArgumentException e) {
            log.error("Validation error while adding product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error adding product", e);
            throw new RuntimeException("Error adding product", e);
        }
    }

    @Override
    @Transactional
    public ProductResponseDto updateImage(Long productId, MultipartFile image) {
        try {
            ProductDetails product = repo.findById(productId)
                    .orElseThrow(() -> {
                        log.warn("Product not found for id: {}", productId);
                        return new RuntimeException("Product not found");
                    });

            if (image == null || image.isEmpty()) {
                log.warn("Image file is empty for update");
                throw new IllegalArgumentException("Image required");
            }

            File folder = new File(IMAGE_BASE_PATH);
            if (!folder.exists()) folder.mkdirs();

            String originalName = Objects.requireNonNull(image.getOriginalFilename())
                    .replaceAll("\\s+", "_");
            String finalImageName = product.getProductId() + "_" + System.currentTimeMillis() + "_" + originalName;
            File dest = new File(IMAGE_BASE_PATH + finalImageName);
            image.transferTo(dest);

            if (product.getImagePath() != null) {
                File oldFile = new File(product.getImagePath());
                if (oldFile.exists()) {
                    if (oldFile.delete()) {
                        log.info("Old image deleted successfully for productId: {}", productId);
                    } else {
                        log.warn("Failed to delete old image for productId: {}", productId);
                    }
                }
            }

            product.setImageName(finalImageName);
            product.setImagePath(IMAGE_BASE_PATH + finalImageName);
            product.setImageUrl("/api/files/view/image/" + finalImageName);
            product.setUpdatedAt(LocalDateTime.now());

            repo.save(product);
            log.info("Product image updated successfully for productId: {}", productId);
            return mapper.toDto(product);

        } catch (IllegalArgumentException e) {
            log.error("Validation error while updating image: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to update image for productId: {}", productId, e);
            throw new RuntimeException("Failed to update image", e);
        }
    }

    @Override
    public byte[] getImageByProductId(String productId) {
        try {
            ProductDetails product = repo.findByProductId(productId)
                    .orElseThrow(() -> {
                        log.warn("Product not found for id: {}", productId);
                        return new RuntimeException("Product not found");
                    });

            Path path = Paths.get(product.getImagePath());
            return Files.readAllBytes(path);

        } catch (IOException e) {
            log.error("Image not found for productId: {}", productId, e);
            throw new RuntimeException("Image not found for product id: " + productId, e);
        }
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        try {
            List<ProductResponseDto> products = repo.findAll()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
            log.info("Fetched all products, total count: {}", products.size());
            return products;
        } catch (Exception e) {
            log.error("Error fetching all products", e);
            throw new RuntimeException("Error fetching products", e);
        }
    }
}
