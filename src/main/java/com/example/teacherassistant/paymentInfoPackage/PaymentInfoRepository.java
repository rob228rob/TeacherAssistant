package com.example.teacherassistant.paymentInfoPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {

    Optional<PaymentInfo> findById(long id);

    @Query("SELECT p FROM student_payment p WHERE p.student.phone=:phoneNumber")
    Optional<PaymentInfo> findPaymentInfoByStudentPhoneNumber(String phoneNumber);

    @Query("SELECT p FROM student_payment p WHERE p.student.id = :studentId")
    Collection<PaymentInfo> findPaymentInfoByStudentId(long studentId);

    @Modifying
    @Query("DELETE FROM student_payment sp WHERE sp.student.id = :studentId")
    void deleteAllByStudentId(long studentId);
}
