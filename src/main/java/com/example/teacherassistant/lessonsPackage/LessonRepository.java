package com.example.teacherassistant.lessonsPackage;

import com.example.teacherassistant.studentPackage.Student;
import com.example.teacherassistant.teacherPackage.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Collection<Lesson> findAllByStudent(Student student);

    Collection<Lesson> findAllByTeacher(Teacher teacher);

    Collection<Lesson> findAllByDate(LocalDate date);

    @Query("SELECT l FROM lessons l WHERE l.student.id = :id")
    Collection<Lesson> getAllLessonsByStudentId(long id);

    @Query("SELECT l FROM lessons l WHERE l.student.id = :id ORDER BY l.startTime DESC")
    Collection<Lesson> getAllLessonsByStudentIdOrderByTimeExpired(long id);

    @Query("SELECT l FROM lessons l WHERE l.teacher.id = :teacherId")
    Collection<Lesson> getAllLessonsByTeacherId(long teacherId);
}
