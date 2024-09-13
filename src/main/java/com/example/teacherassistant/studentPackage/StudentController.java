package com.example.teacherassistant.studentPackage;

import com.example.teacherassistant.common.dtos.ErrorHandler;
import com.example.teacherassistant.common.dtos.PaymentInfoDTO;
import com.example.teacherassistant.common.myExceptions.*;
import com.example.teacherassistant.imagePackage.StudentImage;
import com.example.teacherassistant.paymentInfoPackage.PaymentInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

        try {
            studentService.addStudent(requestStudentDTO, principal.getName());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeacherNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get/{studentId}")
    public ResponseEntity<?> getStudentByPhoneNumber(@PathVariable long studentId, Principal principal) {
        try {
            ResponseStudentDTO studentDTO = studentService.getStudentByIdReturningDTO(studentId, principal.getName());
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (StudentNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(404, "Student with this id does not exist: " + studentId), HttpStatus.NOT_FOUND);
        } catch (InvalidTeacherCredentials e) {
            return new ResponseEntity<>(new ErrorHandler(403, "Invalid teacher credentials: " + e.getMessage()), HttpStatus.FORBIDDEN);
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
    public ResponseEntity<?> deleteStudentById(@RequestParam long studentId, Principal principal) {
        try {
            studentService.deleteStudentById(studentId, principal.getName());
        } catch (TeacherNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(HttpStatus.NOT_FOUND.value(), "Teacher not found: " + principal.getName()), HttpStatus.NOT_FOUND);
        } catch (InvalidTeacherCredentials e) {
            return new ResponseEntity<>(new ErrorHandler(403, "Invalid teacher credentials: " + e.getMessage()), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<?> updateStudent(@RequestParam(name = "student_id") long studentId,
                                           @RequestBody RequestStudentDTO requestStudentDTO,
                                           Principal principal) {
        try {
            studentService.updateStudentPartly(requestStudentDTO, studentId, principal.getName());
            return ResponseEntity.ok().build();
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidStudentDataException e) {
            return new ResponseEntity<>(new ErrorHandler(400, "Update was interrupted, cause: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/payment-info/{studentId}")
    public ResponseEntity<?> getPaymentInfoByStudentId(@PathVariable long studentId) {
        try {
            var paymentInfoDTO = paymentInfoService.getStudentPaymentInfoByStudentId(studentId);
            if (paymentInfoDTO.isPresent()) {
                return new ResponseEntity<>(paymentInfoDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorHandler(404, "Payment info of student with id: " + studentId + " not found"), HttpStatus.NOT_FOUND);
            }
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/payment-info/{studentId}")
    public ResponseEntity<?> addPaymentInfo(
            @PathVariable long studentId,
            @RequestBody PaymentInfoDTO paymentInfoDTO) {
        try {
            paymentInfoDTO.validatePaymentInfoDTO();
            paymentInfoService.savePaymentInfoByStudentId(paymentInfoDTO, studentId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (InvalidPaymentInfoDataException | StudentNotFoundException e) {
            return new ResponseEntity<>(new ErrorHandler(400, "Invalid payment info: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
