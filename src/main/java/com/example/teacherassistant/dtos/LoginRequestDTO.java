package com.example.teacherassistant.dtos;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String phoneNumber;

    private String password;
}
