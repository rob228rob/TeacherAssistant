package com.example.teacherassistant.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseTeacherDTO {
    private String name;

    private String surname;

    private String phoneNumber;

    private String primarySubject;
}
