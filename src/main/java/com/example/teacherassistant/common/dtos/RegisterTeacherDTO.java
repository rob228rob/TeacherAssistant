package com.example.teacherassistant.common.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterTeacherDTO {
    private String name;

    private String surname;

    private String phoneNumber;

    private String primarySubject;

    private String password;
}