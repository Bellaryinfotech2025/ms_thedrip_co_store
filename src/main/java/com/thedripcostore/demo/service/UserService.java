package com.thedripcostore.demo.service;

import com.thedripcostore.demo.dto.UserDto;

public interface UserService {
 UserDto saveUser(UserDto userDto);
 UserDto getUserByUserId(String userId);
}