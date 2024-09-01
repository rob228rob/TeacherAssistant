package com.example.teacherassistant.services;

import com.example.teacherassistant.dtos.PaymentInfoDTO;
import com.example.teacherassistant.dtos.ResponseSalaryDTO;
import com.example.teacherassistant.entities.PaymentInfo;
import com.example.teacherassistant.entities.Student;
import com.example.teacherassistant.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.repositories.PaymentInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentInfoService {

    private final PaymentInfoRepository paymentInfoRepository;
    private final ModelMapper modelMapper;
    private final StudentService studentService;

    @Transactional
    public Optional<PaymentInfoDTO> savePaymentInfo(PaymentInfoDTO paymentInfo, String studentPhoneNumber) throws StudentNotFoundException {
        PaymentInfo paymentInfoEntity = modelMapper.map(paymentInfo, PaymentInfo.class);
        Student student = studentService.getStudentByPhoneNumber(studentPhoneNumber).orElseThrow(
                () -> new StudentNotFoundException(studentPhoneNumber));
        paymentInfoEntity.setStudent(student);
        student.setPaymentInfo(paymentInfoEntity);

        return Optional.of(modelMapper.map(paymentInfoRepository.save(paymentInfoEntity), PaymentInfoDTO.class));
    }

    public Optional<PaymentInfoDTO> getStudentPaymentInfoByTeacherPhone(String phoneNumber) throws StudentNotFoundException {
        return Optional.of(
                modelMapper.map(
                        paymentInfoRepository.findPaymentInfoByStudentPhoneNumber(phoneNumber).orElseThrow(
                                () -> new StudentNotFoundException("Student with phone: " + phoneNumber + " not found")
                        ),
                        PaymentInfoDTO.class)
        );
    }

    public Optional<ResponseSalaryDTO> getAllPaymentInfoByTeacherPhone(String phoneNumber) throws StudentNotFoundException, TeacherNotFoundException {
        Collection<Student> allStudentsByTeacherPhone = studentService.getCollectionStudentsByTeacherPhone(phoneNumber);

        var returnObj = Optional.of(allStudentsByTeacherPhone.stream()
                .map(x -> {
                    var paymentInfo = x.getPaymentInfo();
                    if (paymentInfo == null) {
                        log.warn("PAYMENT INFO IS NULL for: {}", x.getPhone());
                        return new ResponseSalaryDTO(0.0,0.0,0);
                    }

                    double pricePerMonth = paymentInfo.calculatePricePerMonth();
                    double pricePerWeek = paymentInfo.calculatePricePerWeek();
                    int lessonsPerMonth = paymentInfo.lessonsPerMonth();

                    return ResponseSalaryDTO.builder()
                            .weeklySalary(pricePerWeek)
                            .monthlySalary(pricePerMonth)
                            .lessonsPerMonth(lessonsPerMonth)
                            .build();
                })
                .reduce(
                        new ResponseSalaryDTO(0.0, 0.0, 0),
                        (acc, salaryInfo) -> {
                            salaryInfo.setWeeklySalary(acc.getWeeklySalary() + salaryInfo.getWeeklySalary());
                            salaryInfo.setMonthlySalary(acc.getMonthlySalary() + salaryInfo.getMonthlySalary());
                            salaryInfo.setLessonsPerMonth(acc.getLessonsPerMonth() + salaryInfo.getLessonsPerMonth());
                            return salaryInfo;
                        },
                        (acc1, acc2) -> {
                            acc1.setWeeklySalary(acc1.getWeeklySalary() + acc2.getWeeklySalary());
                            acc1.setMonthlySalary(acc1.getMonthlySalary() + acc2.getMonthlySalary());
                            acc1.setLessonsPerMonth(acc1.getLessonsPerMonth() + acc2.getLessonsPerMonth());
                            return acc2;
                        }));
        System.out.println("---------------------------");
        log.info(returnObj.get().toString());
        System.out.println("---------------------------");
        return returnObj;
    }
}
