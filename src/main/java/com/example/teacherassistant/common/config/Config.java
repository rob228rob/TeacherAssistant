package com.example.teacherassistant.common.config;

import com.example.teacherassistant.common.services.FileManager;
import com.example.teacherassistant.common.services.FilesystemFileManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FileManager fileManager() {
        return new FilesystemFileManager();
    }
}
