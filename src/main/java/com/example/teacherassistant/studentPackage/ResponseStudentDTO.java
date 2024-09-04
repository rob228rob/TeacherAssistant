package com.example.teacherassistant.studentPackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStudentDTO {

    private long id;

    private String name;

    private String surname;

    private String phone;

    private String email;

    private String purposeDescription;

    private int grade;

    private List<Long> imageIds;
}
