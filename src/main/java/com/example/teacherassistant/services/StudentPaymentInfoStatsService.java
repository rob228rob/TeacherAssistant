package com.example.teacherassistant.services;

import com.example.teacherassistant.entities.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentPaymentInfoStatsService {

    private final StudentService studentService;

    private final TeacherService teacherService;

    public Double getTeacherSalaryByMonth(String teacherPhoneNumber) {
        Collection<Student> studentsByTeacher = studentService.getCollectionStudentsByTeacherPhone(teacherPhoneNumber);

        return studentsByTeacher.stream()
                .map(x -> {
                    var paymentInfo = x.getPaymentInfo();
                    return (Double) paymentInfo.calculatePricePerMonth();
                })
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public Double getMonthlyPriceByStudent(String studentPhoneNumber, String teacherPhoneNumber) {
        Optional<Student> studentByPhoneNumber = studentService.findStudentByPhoneNumber(studentPhoneNumber, teacherPhoneNumber);
        return studentByPhoneNumber
                .map(student -> student.getPaymentInfo().calculatePricePerMonth())
                .orElse(0.0);

    }
}
