package com.example.teacherassistant.lessonsPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LessonResponseDTO implements Serializable {

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