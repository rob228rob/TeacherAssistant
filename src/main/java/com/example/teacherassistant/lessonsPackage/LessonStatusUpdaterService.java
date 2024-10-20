package com.example.teacherassistant.lessonsPackage;

import com.example.teacherassistant.studentPackage.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonStatusUpdaterService {

    private final LessonRepository lessonRepository;

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void updateLessonStatuses() {
        var allLessons = lessonRepository.findAll()
                .stream()
                .peek(this::updateLessonStatus)
                .toList();

        lessonRepository.saveAll(allLessons);
    }

    private void updateLessonStatus(Lesson lesson) {
        if (lesson.getStatus() == LessonStatus.CANCELLED) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();


        if (lesson.getDate().equals(today)) {
            if (now.isBefore(lesson.getStartTime())) {
                lesson.setStatus(LessonStatus.PENDING);
            } else if (now.isAfter(lesson.getEndTime())) {
                lesson.setStatus(LessonStatus.COMPLETED);
            } else {
                lesson.setStatus(LessonStatus.IN_PROGRESS);
            }
        } else if (lesson.getDate().isAfter(today)) {
            lesson.setStatus(LessonStatus.PENDING);
        } else if (lesson.getDate().isBefore(today)) {
            lesson.setStatus(LessonStatus.COMPLETED);
        }
    }
}
