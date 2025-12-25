package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
public class Configure {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //Spring Security-də istifadə olunan interface-dir.
    //Məqsəd: parolları təhlükəsiz saxlamaq və yoxlamaq.
    //Sadə dillə: “İstifadəçinin yazdığı şifrəni kodlayırıq (encrypt) ki, database-də düz mətn kimi saxlanmasın.”
    //Əgər istifadəçi şifrəsini düz mətn kimi saxlasan → təhlükəsizlik riski çox yüksəkdir.
    //Hacker database-ə daxil olsa, bütün şifrələr görünəcək.
    //PasswordEncoder bunu hash-ləyir. Məsələn:
    //Şifrə: 12345
    //Hash: $2a$10$7Q1Vn2L5nPp6LQO7B6Jgxe5U5kq2...
}
