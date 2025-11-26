package com.thedripcostore.demo.dto;


import org.springframework.stereotype.Component;

import com.thedripcostore.demo.entity.ProductDetails;

@Component
public class ProductMapper {

    // Convert DTO → Entity
    public ProductDetails toEntity(ProductRequestDto dto) {
        ProductDetails entity = new ProductDetails();

        entity.setProductId(dto.getProductId());
        entity.setTitle(dto.getTitle());
        entity.setCategory(dto.getCategory());
        entity.setPrice(dto.getPrice());
        entity.setPreviousPrice(dto.getPreviousPrice());
        entity.setStock(dto.getStock() != null ? dto.getStock() : 0);
        entity.setDescription(dto.getDescription());
        entity.setSize(dto.getSize());
        // createdAt / updatedAt will be set in ServiceImpl
        return entity;
    }

    // Convert Entity → DTO
    public ProductResponseDto toDto(ProductDetails entity) {
        ProductResponseDto dto = new ProductResponseDto();

        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setTitle(entity.getTitle());
        dto.setCategory(entity.getCategory());
        dto.setPrice(entity.getPrice());
        dto.setPreviousPrice(entity.getPreviousPrice());
        dto.setStock(entity.getStock());
        dto.setDescription(entity.getDescription());
        dto.setSize(entity.getSize());
        dto.setImageUrl(entity.getImageUrl()); // local path / URL

        return dto;
    }
}
