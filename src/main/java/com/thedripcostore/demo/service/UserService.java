package com.thedripcostore.demo.service;

import java.util.List;

import com.thedripcostore.demo.dto.UserDto;

public interface UserService {
 UserDto saveUser(UserDto userDto);
 UserDto getUserByUserId(String userId);
 
 List<UserDto> getAllUsers();
}