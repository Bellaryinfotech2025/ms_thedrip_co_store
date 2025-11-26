package com.thedripcostore.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thedripcostore.demo.service.ProductService;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/api/files/view")
public class FileController {

    private final ProductService productService;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(FileController.class);

    private static final String IMAGE_BASE_PATH = "C:/drip_images/products/";

    public FileController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/image/{filename:.+}") // allow dots in path variable
    public ResponseEntity<?> viewImage(@PathVariable String filename) {
        try {
            String filePath = IMAGE_BASE_PATH + filename;
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Image not found: " + filename, null));
            }

            byte[] imageBytes = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentLength(imageBytes.length)
                    .body(imageBytes);
        } catch (IOException e) {
            logger.error("viewImage failed for filename: " + filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to read image", e.getMessage()));
        }
    }


    // small internal DTO for clean json error messages
    private static class ApiResponse {
        public String message;
        public Object error;

        public ApiResponse(String message, Object error) {
            this.message = message;
            this.error = error;
        }
    }
}
