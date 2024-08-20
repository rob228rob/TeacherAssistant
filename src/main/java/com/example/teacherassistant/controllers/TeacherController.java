package com.example.teacherassistant.controllers;

import com.example.teacherassistant.dtos.TeacherDTO;
import com.example.teacherassistant.entities.Teacher;
import com.example.teacherassistant.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/t.assist/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherDTO teacherDTO) {
        if (teacherService.existsByPhoneNumber(teacherDTO.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Teacher with this phone number already exists");
        }

        teacherDTO.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
        Teacher newTeacher = teacherService.saveTeacher(teacherDTO);
        return ResponseEntity.ok(newTeacher);
    }

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
    public ResponseEntity<?> updateTeacher(@PathVariable long id, @RequestBody TeacherDTO teacherDTO) {
        return teacherService.updateTeacher(id, teacherDTO)
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
