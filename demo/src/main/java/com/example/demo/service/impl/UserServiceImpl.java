package com.example.demo.service.impl;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.RegisterRequestDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserResponseDto register (RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered"); // ya da custom exception
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        userRepository.save(user);
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
   //İstifadəçi formu doldurur → username = “Ali”
        //request.getUsername() → yazılan adı götürür
        //user.setUsername() → istifadəçi obyektinə qoyur
        //Sonra userRepository.save(user) çağıranda bu ad database-də saxlanır

        //İstifadəçi şifrə yazır → 12345
        //passwordEncoder.encode() → onu kilidləyir (hash) → $2a$10$...
        //user.setPassword() → database-ə hash-lənmiş şifrə yazılır
    }
//
//    @Override
//    public List<UserResponseDto> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        List<UserResponseDto> response= new ArrayList<>();
//        for (User user : users) {
//            response.add(
//                    new UserResponseDto(
//                            user.getId(),
//                            user.getUsername(),
//                            user.getEmail(),
//                            user.getRole()
//                    )
//            );
//        }
//
//        return response;
//    }

//StreamApi
    //@Override
    //public List<UserResponseDto> getAllUsers() {
    //    return userRepository.findAll()
    //                         .stream()  // List<User> → Stream<User>
    //                         .map(user -> new UserResponseDto(
    //                             user.getId(),
    //                             user.getUsername(),
    //                             user.getEmail(),
    //                             user.getRole()
    //                         ))  // Stream<User> → Stream<UserResponseDto>
    //                         .toList(); // Stream → List<UserResponseDto>
    //}




    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
    }




    @Override
    public UserResponseDto login(LoginRequestDto request) {
       User user=userRepository.findByUsername(request.getUsername())
               .orElseThrow(() -> new RuntimeException("Username not found"));

       if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Wrong password");
       }

       return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public UserResponseDto updateUser(Long id, RegisterRequestDto request) {
        User user=userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if(request.getPassword()!=null && !request.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public void deleteUser(Long id) {
       User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
       userRepository.delete(user);
    }

}

