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
//polimorfizm
public class UserServiceImpl implements UserService {
    //enkapsulyasiya
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    //modelmapper-entityni dtoya cevirmek ucun istifade olunur
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserResponseDto register (RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered"); // ya da custom exception
        }
        User user = new User();
        user.setUsername(request.getUsername());//İstifadəçidən gələn requesti götürür və userin usernamesine elave edir yeni usernameni deyisir
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        userRepository.save(user);//yenilədik və saxladıq
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
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

//StreamApi     collection-dakı elementləri rahat, qısa və oxunaqlı şəkildə işləmək üçün istifadə olunur.
    //@Override
    //public List<UserResponseDto> getAllUsers() {
    //    return userRepository.findAll()
    //                         .stream()  // List<User> → Stream<User>
    //                         .map(user -> new UserResponseDto(   //map → stream-dakı hər bir elementi başqa bir elementə çevirmək üçündür.lambda
    //                             user.getId(),
    //                             user.getUsername(),
    //                             user.getEmail(),
    //                             user.getRole()
    //                         ))
    //                         .toList(); // Stream → List<UserResponseDto>
    //}




    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
    }
//modelMapper.map(user, UserResponseDto.class)
//ModelMapper kitabxanası vasitəsilə User entity-sini UserResponseDto-ya avtomatik çevrir.
//map() metodu:
//İlk arqument → çevriləcək obyekt (user)
//İkinci arqument → çevriləcək tip (UserResponseDto.class)
//Bu üsul manual setUsername, setEmail və s. yazmaqdan qurtarır.



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

