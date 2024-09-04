package com.example.teacherassistant.imagePackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<StudentImage, Long> {

    Optional<StudentImage> findByFileName(String fileName);

    List<StudentImage> findAllByOrderByFileName();

    @Query("""
    SELECT i
    FROM images i\s
    WHERE i.student.id =\s
    (SELECT s.id\s
     FROM students s\s
     WHERE s.phone = :phone)
   \s""")
    Optional<StudentImage> findImageByStudentPhoneNumber(@Param("phone") String studentPhoneNumber);

}
