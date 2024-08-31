package com.example.teacherassistant.controllers;

import com.example.teacherassistant.dtos.RegisterTeacherDTO;
import com.example.teacherassistant.dtos.ResponseTeacherDTO;
import com.example.teacherassistant.entities.Teacher;
import com.example.teacherassistant.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.services.PaymentInfoService;
import com.example.teacherassistant.services.StudentPaymentInfoStatsService;
import com.example.teacherassistant.services.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/t.assist/teachers")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;

    private final StudentPaymentInfoStatsService statsPaymentService;

    private final ModelMapper modelMapper;
    private final PaymentInfoService paymentInfoService;

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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTeacherByPhoneNumber(@RequestParam String phoneNumber) {
        teacherService.deleteTeacherByPhone(phoneNumber);
        log.warn("teacher with phone number {} had been deleted", phoneNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentPhone(Principal principal) {
        String phoneNumber = principal.getName();
        Optional<Teacher> studentByPhoneNumber = teacherService.findTeacherByPhone( phoneNumber);
        if (studentByPhoneNumber.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(modelMapper.map(studentByPhoneNumber.get(), ResponseTeacherDTO.class));
    }

    @GetMapping("/get-salary")
    public ResponseEntity<?> getMonthlySalaryByTeacher(Principal principal) {
        String phoneNumber = principal.getName();
        Optional<Teacher> teacherByPhone = teacherService.findTeacherByPhone(phoneNumber);
        if (teacherByPhone.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            return ResponseEntity.ok(paymentInfoService.getAllPaymentInfoByTeacherPhone(phoneNumber));
        } catch (StudentNotFoundException | TeacherNotFoundException e) {
           return ResponseEntity.notFound().build();
        }
    }

}
