package com.example.teacherassistant.lessonsPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponseDTO {

    private Long id;

    private LessonStatus lessonStatus;

    private String title;

    private String subject;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String description;

    @JsonProperty("isHidden")
    private boolean isHidden;

    private Long studentId;
}