package com.thedripcostore.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.thedripcostore.demo.dto.ProductRequestDto;
import com.thedripcostore.demo.dto.ProductResponseDto;

public interface ProductService {

    // add product with local file storage
    ProductResponseDto addProduct(ProductRequestDto dto, MultipartFile image);

    // update only the image
    ProductResponseDto updateImage(String productId, MultipartFile image);
  
    // get all products
    List<ProductResponseDto> getAllProducts();

	byte[] getImageByProductId(String productId);
	
    void deleteProductByProductId(String productId);

}
