package com.example.teacherassistant.services;

import com.example.teacherassistant.dtos.StudentDTO;
import com.example.teacherassistant.entities.Student;
import com.example.teacherassistant.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final ModelMapper modelMapper;

    private final TeacherService teacherService;

    private final StudentRepository studentRepository;

    public Student addStudent(StudentDTO studentDTO) {
        Student student = modelMapper.map(studentDTO, Student.class);

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll().stream().toList();
    }

    public Student getStudentById(long id) throws StudentNotFoundException {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + " not found"));
    }

    public void deleteStudentById(long id) {
        studentRepository.deleteById(id);
    }

    public void deleteAllStudents() {
        studentRepository.deleteAll();
    }

    public void updateStudentById(StudentDTO studentDTO) {
        Student student = modelMapper.map(studentDTO, Student.class);

        studentRepository.save(student);
    }

    public long studentsCount() {
        return studentRepository.count();
    }

    public Student getStudentByEmail(String email) throws StudentNotFoundException {
        return studentRepository.findByEmail(email).orElseThrow(() -> new StudentNotFoundException("There's no student with email: " + email));
    }

    public boolean checkIfStudentExist(StudentDTO studentDTO) {
        boolean existByEmail = studentRepository.existsByEmail(studentDTO.getEmail());
        boolean existByNameAndSurname = studentRepository.existsByNameAndSurname(studentDTO.getName(), studentDTO.getSurname());

        return existByEmail || existByNameAndSurname;
    }
}
