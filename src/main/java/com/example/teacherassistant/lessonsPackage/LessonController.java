package com.example.teacherassistant.lessonsPackage;

import com.example.teacherassistant.common.dtos.ErrorHandler;
import com.example.teacherassistant.common.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.studentPackage.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/t.assist/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/add-new")
    public ResponseEntity<?> addNewLesson(@RequestBody LessonRequestDTO lessonRequestDTO) {
        try {
            lessonRequestDTO.validate();
            return new ResponseEntity<>(
                    lessonService.addNewLessonByStudent(lessonRequestDTO), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/cancel/{lessonId}")
    public ResponseEntity<?> cancelAndHideLesson(@PathVariable long lessonId) {
        try {
            lessonService.updateLessonStatusToCanceled(lessonId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (LessonsNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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

    @GetMapping("/get-all-by-teacher/{teacherId}")
    public ResponseEntity<?> getAllLessonsByStudentByTeacher(@PathVariable long teacherId) {
        try {
            var resultList = lessonService.getAllLessonsByTeacherId(teacherId);
            log.info("\n----------\n---------\n---------");
            resultList.forEach(lesson -> log.info(lesson.toString()));
            log.info("\n----------\n---------\n---------");
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (LessonsNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-all-hidden-by-teacher/{teacherId}")
    public ResponseEntity<?> getAllLessonsByStudentHiddenByTeacher(@PathVariable long teacherId) {
        try {
            return new ResponseEntity<>(lessonService.getAllHiddenLessonsByTeacherId(teacherId), HttpStatus.OK);
        } catch (LessonsNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/hide/{lessonId}")
    public ResponseEntity<?> hideLessonById(@PathVariable long lessonId) {
        try {
            lessonService.hideLessonDisplayingById(lessonId);
            return ResponseEntity.ok().build();
        } catch (LessonsNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/unhide/{lessonId}")
    public ResponseEntity<?> unHideLessonById(@PathVariable long lessonId) {
        try {
            lessonService.unHideLessonDisplayingById(lessonId);
            return ResponseEntity.ok().build();
        } catch (LessonsNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
