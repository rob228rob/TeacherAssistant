package com.example.teacherassistant.controllers;

import com.example.teacherassistant.dtos.ErrorHandler;
import com.example.teacherassistant.dtos.StudentDTO;
import com.example.teacherassistant.entities.Student;
import com.example.teacherassistant.myExceptions.InvalidStudentDataException;
import com.example.teacherassistant.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.services.StudentService;
import jakarta.websocket.server.PathParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/t.assist/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private final ModelMapper modelMapper;

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(studentService.studentsCount(), HttpStatus.OK);
    }

    @GetMapping("/")
    public String healthCheck() throws InvalidStudentDataException {

        return "Health Check OK";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/add-new")
    public ResponseEntity<?> addStudent(@RequestBody StudentDTO studentDTO, Principal principal) {
        try {
            studentDTO.validateStudentDTO();
            boolean isExist = studentService.checkIfStudentExist(studentDTO);
            if (isExist) {
                return new ResponseEntity<>(new ErrorHandler(HttpStatus.CONFLICT.value(), "User with this email or name already exists: " + studentDTO.getEmail() + " ; name : " + studentDTO.getName() + " " + studentDTO.getSurname()), HttpStatus.CONFLICT);
            }
        } catch (InvalidStudentDataException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        boolean addedSuccessfully = studentService.addStudent(studentDTO, principal.getName());

        //TODO: !!! Rewrite with informative error message
        return addedSuccessfully
                ? ResponseEntity.ok().build()
                : new ResponseEntity<>(new ErrorHandler(400, "Adding was interrupted"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable long id) {
        try {
            Student studentById = studentService.getStudentById(id);

            return ResponseEntity.ok(studentById);
        } catch (StudentNotFoundException e) {
            log.error(e.getMessage());

            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get-all")
    public ResponseEntity<?> getAllStudents(Principal principal) {
        try {
            var allStudents = studentService.getAllStudentsByTeacherPhone(principal.getName());

            return allStudents.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(allStudents);
        } catch (TeacherNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(HttpStatus.NOT_FOUND.value(), "Teacher not found: " + principal.getName()), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete")
    public ResponseEntity<?> deleteStudentById(@RequestParam String phoneNumber) {
        if (!validatePhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body("Invalid phone number: " + phoneNumber);
        }

        studentService.deleteStudentByPhone(phoneNumber);

        return ResponseEntity.noContent().build();
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return false;
        }

        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete-all")
    public ResponseEntity<?> deleteAllStudents() {
        if (studentService.studentsCount() > 0) {
            studentService.deleteAllStudents();
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete-by-ids")
    public ResponseEntity<?> deleteStudentsByIds(@RequestBody Collection<Long> ids) {
        if (ids.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ids.forEach(studentService::deleteStudentById);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/update")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDTO studentDTO) {
        studentService.updateStudentById(studentDTO);

        return ResponseEntity.ok().build();
    }


}
