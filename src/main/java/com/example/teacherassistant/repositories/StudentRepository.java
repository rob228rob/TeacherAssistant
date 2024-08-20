package com.example.teacherassistant.repositories;

import com.example.teacherassistant.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByNameAndSurname(String name, String surname);

    void deleteByNameAndSurname(String name, String surname);

    Optional<Student> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNameAndSurname(String name, String surname);
}
