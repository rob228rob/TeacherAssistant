package com.example.teacherassistant.controllers;

import com.example.teacherassistant.dtos.RegisterTeacherDTO;
import com.example.teacherassistant.entities.Teacher;
import com.example.teacherassistant.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/t.assist/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable long id) {
        return teacherService.getTeacherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> allTeachers = teacherService.getAllTeachers();
        return allTeachers.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(allTeachers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable long id, @RequestBody RegisterTeacherDTO registerTeacherDTO) {
        return teacherService.updateTeacher(id, registerTeacherDTO)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable long id) {
        if (teacherService.deleteTeacherById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
