package com.example.teacherassistant.lessonsPackage;

import com.example.teacherassistant.common.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.studentPackage.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/t.assist/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/add-new")
    public ResponseEntity<?> addNewLesson(@RequestBody LessonRequestDTO lessonRequestDTO) {
        try {
            //lessonRequestDTO.validate();
            lessonService.addNewLessonByStudent(lessonRequestDTO);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error(e.getMessage());
            log.error(lessonRequestDTO.toString());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-all/{studentId}")
    public ResponseEntity<?> getAllLessonsByStudent(@PathVariable long studentId) {
        try {
            return new ResponseEntity<>(lessonService.getAllLessonsByStudentId(studentId), HttpStatus.OK);
        } catch (LessonsNotFoundException | StudentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-all/sorted/{studentId}")
    public ResponseEntity<?> getAllLessonsByStudentSorted(@PathVariable long studentId) {
        try {
            return new ResponseEntity<>(lessonService.getAllLessonsByStudentIdOrderByTime(studentId), HttpStatus.OK);
        } catch (LessonsNotFoundException | StudentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
