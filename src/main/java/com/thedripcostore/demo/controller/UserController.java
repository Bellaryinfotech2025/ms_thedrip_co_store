package com.thedripcostore.demo.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thedripcostore.demo.dto.UserDto;
import com.thedripcostore.demo.entity.UserEntity;
import com.thedripcostore.demo.repository.UserRepository;
import com.thedripcostore.demo.service.UserService;

@RestController
@RequestMapping("/api/v2")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

 private static final Logger logger = LoggerFactory.getLogger(UserController.class);

 @Autowired
 private UserService userService;
@Autowired 
private UserRepository userRepository;


 @PostMapping("/users")
 public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto, HttpServletResponse response) {
     try {
         logger.info("Received POST request to create user with name: {}", userDto.getName());

         UserDto savedUser = userService.saveUser(userDto);

         // Set userId in cookie
         Cookie cookie = new Cookie("userId", savedUser.getUserId());
         cookie.setMaxAge(3600); // 1 hour
         cookie.setPath("/");
         response.addCookie(cookie);

         logger.info("User created and cookie set with userId: {}", savedUser.getUserId());
         return ResponseEntity.ok(savedUser);
     } catch (Exception e) {
         logger.error("Error in POST /users: {}", e.getMessage(), e);
         return ResponseEntity.internalServerError().build();
     }
 }

 @GetMapping("/users/{userId}")
 public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
     try {
         logger.info("Received GET request for user with userId: {}", userId);

         UserDto user = userService.getUserByUserId(userId);

         logger.info("User retrieved successfully with userId: {}", userId);
         return ResponseEntity.ok(user);
     } catch (Exception e) {
         logger.error("Error in GET /users/{}: {}", userId, e.getMessage(), e);
         return ResponseEntity.notFound().build();
     }
 }
 
 
 
 
 @GetMapping("/users")
 public ResponseEntity<List<UserDto>> getAllUsers() {
     try {
         logger.info("Received GET request for all users");
         List<UserDto> users = userService.getAllUsers();
         return ResponseEntity.ok(users);
     } catch (Exception e) {
         logger.error("Error fetching all users: {}", e.getMessage(), e);
         return ResponseEntity.internalServerError().build();
     }
 }

}