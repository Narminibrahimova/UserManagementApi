package com.example.demo.service;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.RegisterRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.User;

import java.util.List;
//abstraksiya
public interface UserService {
    UserResponseDto register (RegisterRequestDto request);
    List<UserResponseDto> getAllUsers();
    UserResponseDto login (LoginRequestDto request);


    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(Long id, RegisterRequestDto request);
    void deleteUser(Long id);
}
