package com.example.teacherassistant.paymentInfoPackage;

import com.example.teacherassistant.teacherPackage.TeacherService;
import com.example.teacherassistant.studentPackage.Student;
import com.example.teacherassistant.studentPackage.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

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
