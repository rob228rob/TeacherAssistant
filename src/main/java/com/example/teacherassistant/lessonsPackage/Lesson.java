package com.example.teacherassistant.lessonsPackage;

import com.example.teacherassistant.studentPackage.Student;
import com.example.teacherassistant.teacherPackage.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "lessons")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    private LessonStatus status;  // Новый атрибут для отслеживания статуса урока (например, "IN_PROGRESS", "COMPLETED")

    @Column(name = "description")
    private String description;  // Описание урока/таска для дополнительных деталей

    @Column(name = "is_hidden")
    private boolean isHidden;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public void cancelLesson() {
        if (this.status == null) {
            return;
        }

        this.status = LessonStatus.CANCELLED;
    }

    public void updateStatus() {
        if (this.status == LessonStatus.CANCELLED) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (date.isAfter(today)) {

            this.status = LessonStatus.PENDING;
        } else if (date.isBefore(today)) {

            this.status = LessonStatus.COMPLETED;
        } else {

            if (now.isBefore(startTime)) {
                this.status = LessonStatus.PENDING;
            } else if (now.isAfter(endTime)) {
                this.status = LessonStatus.COMPLETED;
            } else {
                this.status = LessonStatus.IN_PROGRESS;
            }
        }
    }

}
