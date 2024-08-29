package com.example.teacherassistant.repositories;

import com.example.teacherassistant.entities.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {

    Optional<PaymentInfo> findById(long id);

    @Query("SELECT p FROM student_payment p WHERE p.student.phone=:phoneNumber")
    Optional<PaymentInfo> findPaymentInfoByStudentPhoneNumber(String phoneNumber);
}
