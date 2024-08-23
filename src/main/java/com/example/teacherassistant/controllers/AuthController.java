package com.example.teacherassistant.controllers;

import com.example.teacherassistant.dtos.LoginRequestDTO;
import com.example.teacherassistant.dtos.RegisterTeacherDTO;
import com.example.teacherassistant.entities.Teacher;
import com.example.teacherassistant.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.services.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TeacherService teacherService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerTeacher(@RequestBody RegisterTeacherDTO registerTeacherDTO) {
        if (teacherService.existsByPhoneNumber(registerTeacherDTO.getPhoneNumber())) {
            log.debug("teacher with number: {} already exists", registerTeacherDTO.getPhoneNumber());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Teacher with this phone number already exists");
        }

        log.info("teacher with number: {} saved successfully", registerTeacherDTO.getPhoneNumber());
        Teacher newTeacher = teacherService.saveTeacher(registerTeacherDTO);
        return ResponseEntity.ok(newTeacher);
    }

    //TODO: TEST LOGIN AND Inject Principial principial into StudentController!!!
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getPhoneNumber(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            var teacher = teacherService.findTeacherByPhone(loginRequest.getPhoneNumber());

            if (teacher.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher with this phone number does not exist: " + loginRequest.getPhoneNumber());
            }

            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password");
        }
    }
}
