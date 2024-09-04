package com.example.teacherassistant.teacherPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByNameAndSurname(String teacherName, String surname);

    Optional<Teacher> findByPhoneNumber(String phone);

    boolean existsByPhoneNumber(String phone);

    /*
    TODO: deleting by Phone Number NoT Work CORRECTLY!!!!
     */
//    @Modifying
//    @Transactional
//    @Query(value = "DELETE FROM teachers AS t WHERE t.phone_number=?", nativeQuery = true)
    @Transactional
    void deleteByPhoneNumber(String phoneNumber);
}

