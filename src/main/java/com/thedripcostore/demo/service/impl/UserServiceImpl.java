package com.thedripcostore.demo.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thedripcostore.demo.dto.UserDto;
import com.thedripcostore.demo.entity.UserEntity;
import com.thedripcostore.demo.repository.UserRepository;
import com.thedripcostore.demo.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

 private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

 @Autowired
 private UserRepository userRepository;

 @Override
 public UserDto saveUser(UserDto userDto) {
     try {
         logger.info("Starting to save user with name: {}", userDto.getName());

         UserEntity entity = new UserEntity();
         entity.setName(userDto.getName());
         entity.setPhoneNumber(userDto.getPhoneNumber());

         // Generate 6-digit random userId
         Random rand = new Random();
         int randomNum = 100000 + rand.nextInt(900000);
         String userId = String.valueOf(randomNum);
         entity.setUserId(userId);

         entity.setRegisteredDateTime(LocalDateTime.now());
         entity.setStatus("active");

         UserEntity savedEntity = userRepository.save(entity);

         UserDto savedDto = new UserDto();
         savedDto.setId(savedEntity.getId());
         savedDto.setName(savedEntity.getName());
         savedDto.setPhoneNumber(savedEntity.getPhoneNumber());
         savedDto.setUserId(savedEntity.getUserId());
         savedDto.setRegisteredDateTime(savedEntity.getRegisteredDateTime());
         savedDto.setStatus(savedEntity.getStatus());

         logger.info("User saved successfully with userId: {}", savedDto.getUserId());
         return savedDto;
     } catch (Exception e) {
         logger.error("Error saving user: {}", e.getMessage(), e);
         throw new RuntimeException("Failed to save user", e);
     }
 }

 @Override
 public UserDto getUserByUserId(String userId) {
     try {
         logger.info("Fetching user with userId: {}", userId);

         Optional<UserEntity> optionalEntity = userRepository.findByUserId(userId);
         if (optionalEntity.isEmpty()) {
             logger.warn("User not found with userId: {}", userId);
             throw new RuntimeException("User not found");
         }

         UserEntity entity = optionalEntity.get();

         UserDto dto = new UserDto();
         dto.setId(entity.getId());
         dto.setName(entity.getName());
         dto.setPhoneNumber(entity.getPhoneNumber());
         dto.setUserId(entity.getUserId());
         dto.setRegisteredDateTime(entity.getRegisteredDateTime());
         dto.setStatus(entity.getStatus());

         logger.info("User fetched successfully with userId: {}", userId);
         return dto;
     } catch (Exception e) {
         logger.error("Error fetching user: {}", e.getMessage(), e);
         throw new RuntimeException("Failed to fetch user", e);
     }
 }
 
 
 @Override
 public List<UserDto> getAllUsers() {
     try {
         logger.info("Fetching all users");
         List<UserEntity> entities = userRepository.findAll();
         
         return entities.stream().map(entity -> {
             UserDto dto = new UserDto();
             dto.setId(entity.getId());
             dto.setName(entity.getName());
             dto.setPhoneNumber(entity.getPhoneNumber());
             dto.setUserId(entity.getUserId());
             dto.setRegisteredDateTime(entity.getRegisteredDateTime());
             dto.setStatus(entity.getStatus());
             return dto;
         }).toList();
         
     } catch (Exception e) {
         logger.error("Error fetching all users: {}", e.getMessage(), e);
         throw new RuntimeException("Failed to fetch users", e);
     }
 }
}