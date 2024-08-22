package com.example.teacherassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class TeacherAssistantApplication {

    public static void main(String[] args) {

        //File file = new File("newone.txt");

        SpringApplication.run(TeacherAssistantApplication.class, args);
    }

}
