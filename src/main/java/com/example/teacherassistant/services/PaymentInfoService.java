package com.example.teacherassistant.services;

import com.example.teacherassistant.dtos.PaymentInfoDTO;
import com.example.teacherassistant.entities.PaymentInfo;
import com.example.teacherassistant.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.repositories.PaymentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentInfoService {

    private final PaymentInfoRepository paymentInfoRepository;
    private final ModelMapper modelMapper;
    private final StudentService studentService;

    public Optional<PaymentInfoDTO> savePaymentInfo(PaymentInfoDTO paymentInfo, String studentPhoneNumber) throws StudentNotFoundException {
        PaymentInfo paymentInfoEntity = modelMapper.map(paymentInfo, PaymentInfo.class);
        paymentInfoEntity.setStudent(studentService.getStudentByPhoneNumber(studentPhoneNumber).orElseThrow(
        () -> new StudentNotFoundException(studentPhoneNumber)
        ));

        return Optional.of(modelMapper.map(paymentInfoRepository.save(paymentInfoEntity), PaymentInfoDTO.class));
    }

    public Optional<PaymentInfoDTO> getPaymentInfoByStudentPhone(String phoneNumber) throws StudentNotFoundException {
        return Optional.of(
                modelMapper.map(
                        paymentInfoRepository.findPaymentInfoByStudentPhoneNumber(phoneNumber).orElseThrow(
                                () -> new StudentNotFoundException("Student with phone: " + phoneNumber + " not found")
                        ),
                        PaymentInfoDTO.class)
        );
    }
}
