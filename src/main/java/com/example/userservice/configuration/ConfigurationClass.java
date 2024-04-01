package com.example.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
public class ConfigurationClass {
@Bean
    public BCryptPasswordEncoder createdBcryptEncoder(){
        return new BCryptPasswordEncoder(20);
    }
}
