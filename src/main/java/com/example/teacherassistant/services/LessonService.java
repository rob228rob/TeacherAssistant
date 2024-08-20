package com.example.teacherassistant.services;

import com.example.teacherassistant.entities.Lesson;
import com.example.teacherassistant.entities.Student;
import com.example.teacherassistant.repositories.LessonRepository;
import com.example.teacherassistant.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    private final StudentService studentService;

}
