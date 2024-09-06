package com.example.teacherassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.File;

@EnableCaching
@SpringBootApplication
public class TeacherAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherAssistantApplication.class, args);
    }

}
