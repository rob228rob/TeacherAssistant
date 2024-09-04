package com.example.teacherassistant.lessonsPackage;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequestDTO {
    private Long studentId;

    private String title;

    private String subject;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String description;

    public void validate() {
        this
                .validateTitle()
                .validateSubject();
    }

    //TODO: change IAE to my personal exception

    private LessonRequestDTO validateSubject() {
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }

        return this;
    }

    private LessonRequestDTO validateTitle() {
        if (this.title == null || this.title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        return this;
    }
}
