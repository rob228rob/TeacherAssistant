package com.example.teacherassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class TeacherAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherAssistantApplication.class, args);
    }

}
