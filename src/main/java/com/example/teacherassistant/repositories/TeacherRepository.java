package com.example.teacherassistant.repositories;

import com.example.teacherassistant.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByNameAndSurname(String teacherName, String surname);

    Optional<Teacher> findByPhoneNumber(String phone);

    boolean existsByPhoneNumber(String phone);

    /*
    TODO: deleteing by Phone Number NoT Work CORRECTLY!!!!
     */
    void deleteByPhoneNumber(String phoneNumber);
}

