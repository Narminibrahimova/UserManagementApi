package com.example.demo.service;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.RegisterRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDto register (RegisterRequestDto request);
    List<User> getAllUsers();
    UserResponseDto login (LoginRequestDto request);
}
