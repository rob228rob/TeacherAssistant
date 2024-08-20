package com.example.teacherassistant.repositories;

import com.example.teacherassistant.entities.Lesson;
import com.example.teacherassistant.entities.Student;
import com.example.teacherassistant.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Collection<Lesson> findAllByStudent(Student student);

    Collection<Lesson> findAllByTeacher(Teacher teacher);

    Collection<Lesson> findAllByDate(LocalDate date);
}
