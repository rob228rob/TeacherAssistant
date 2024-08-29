package com.example.teacherassistant.controllers;

import com.example.teacherassistant.dtos.ErrorHandler;
import com.example.teacherassistant.dtos.PaymentInfoDTO;
import com.example.teacherassistant.dtos.RequestStudentDTO;
import com.example.teacherassistant.dtos.ResponseStudentDTO;
import com.example.teacherassistant.entities.PaymentInfo;
import com.example.teacherassistant.entities.Student;
import com.example.teacherassistant.entities.StudentImage;
import com.example.teacherassistant.myExceptions.InvalidPaymentInfoDataException;
import com.example.teacherassistant.myExceptions.InvalidStudentDataException;
import com.example.teacherassistant.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.services.PaymentInfoService;
import com.example.teacherassistant.services.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/t.assist/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private final ModelMapper modelMapper;

    private final PaymentInfoService paymentInfoService;

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(studentService.studentsCount(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/add-new")
    public ResponseEntity<?> addStudent(@RequestBody RequestStudentDTO requestStudentDTO, Principal principal) {
        try {
            requestStudentDTO.validateStudentDTO();
            boolean isExist = studentService.checkIfStudentExist(requestStudentDTO);
            if (isExist) {
                return new ResponseEntity<>(new ErrorHandler(HttpStatus.CONFLICT.value(), "User with this email or name already exists: " + requestStudentDTO.getEmail() + " ; name : " + requestStudentDTO.getName() + " " + requestStudentDTO.getSurname()), HttpStatus.CONFLICT);
            }
        } catch (InvalidStudentDataException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        boolean addedSuccessfully = studentService.addStudent(requestStudentDTO, principal.getName());

        //TODO: !!! Rewrite with informative error message
        return addedSuccessfully
                ? ResponseEntity.ok().build()
                : new ResponseEntity<>(new ErrorHandler(400, "Adding was interrupted"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get/{phone_number}")
    public ResponseEntity<?> getStudentByPhoneNumber(@PathVariable String phone_number) {
        var studentById = studentService.findStudentByPhoneNumber(phone_number);
        if (studentById.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var requestStudentDTO = modelMapper.map(studentById.get(), ResponseStudentDTO.class);
        List<Long> listImagesIds = studentById.get().getStudentImages().stream()
                .map(StudentImage::getId)
                .toList();
        requestStudentDTO.setImageIds(listImagesIds);

        return ResponseEntity.ok(requestStudentDTO);
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
        if (!studentService.validatePhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body("Invalid phone number: " + phoneNumber);
        }

        studentService.deleteStudentByPhone(phoneNumber);

        return ResponseEntity.noContent().build();
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

    @PatchMapping(path = "/update")
    public ResponseEntity<?> updateStudent(@RequestParam(name = "phone") String phoneNumber, @RequestBody RequestStudentDTO requestStudentDTO) {

        if (!studentService.validatePhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body("Invalid phone number: " + phoneNumber);
        }

        try {
            studentService.updateStudentPartly(requestStudentDTO, phoneNumber);
            return ResponseEntity.ok().build();
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidStudentDataException e) {
            return new ResponseEntity<>(new ErrorHandler(400, "Update was interrupted, cause: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/payment-info/{phone}")
    public ResponseEntity<?> getPaymentInfo(@PathVariable String phone) {
        if (!studentService.validatePhoneNumber(phone)) {
            return ResponseEntity.badRequest().body("Invalid phone number: " + phone);
        }

        try {
            var paymentInfoDTO = paymentInfoService.getPaymentInfoByStudentPhone(phone);
            if (paymentInfoDTO.isPresent()) {
                return new ResponseEntity<>(paymentInfoDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/payment-info/{phone}")
    public ResponseEntity<?> addPaymentInfo(@PathVariable String phone, @RequestBody PaymentInfoDTO paymentInfoDTO) {
        if (!studentService.validatePhoneNumber(phone)) {
            return ResponseEntity.badRequest().body("Invalid phone number: " + phone);
        }

        try {
            paymentInfoDTO.validatePaymentInfoDTO();
            paymentInfoService.savePaymentInfo(paymentInfoDTO, phone);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (InvalidPaymentInfoDataException | StudentNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(400, "Invalid payment info: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
