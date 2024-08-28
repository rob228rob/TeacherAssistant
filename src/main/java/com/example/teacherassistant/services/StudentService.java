package com.example.teacherassistant.services;

import com.example.teacherassistant.dtos.RequestStudentDTO;
import com.example.teacherassistant.entities.Student;
import com.example.teacherassistant.entities.Teacher;
import com.example.teacherassistant.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final ModelMapper modelMapper;

    private final TeacherService teacherService;

    private final StudentRepository studentRepository;

    public boolean addStudent(RequestStudentDTO requestStudentDTO, String teacherPhone) {
        Optional<Teacher> teacherByPhone = teacherService.findTeacherByPhone(teacherPhone);
        if (teacherByPhone.isEmpty()) {
            return false;
        }
        Student student = modelMapper.map(requestStudentDTO, Student.class);
        student.setTeacher(teacherByPhone.get());
        studentRepository.save(student);

        return true;
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

    public void updateStudentById(RequestStudentDTO requestStudentDTO) {
        Student student = modelMapper.map(requestStudentDTO, Student.class);

        studentRepository.save(student);
    }

    public long studentsCount() {
        return studentRepository.count();
    }

    public Student getStudentByEmail(String email) throws StudentNotFoundException {
        return studentRepository.findByEmail(email).orElseThrow(() -> new StudentNotFoundException("There's no student with email: " + email));
    }

    public boolean checkIfStudentExist(RequestStudentDTO requestStudentDTO) {
        boolean existByEmail = studentRepository.existsByEmail(requestStudentDTO.getEmail());
        boolean existByNameAndSurname = studentRepository.existsByNameAndSurname(requestStudentDTO.getName(), requestStudentDTO.getSurname());

        return existByEmail || existByNameAndSurname;
    }

    public List<RequestStudentDTO> getAllStudentsByTeacherPhone(String phone) throws TeacherNotFoundException {
        Optional<Teacher> teacher = teacherService.findTeacherByPhone(phone);
        if (teacher.isEmpty()) {
            throw new TeacherNotFoundException("teacher with phone: " + phone + " does not exist");
        }

        Collection<Student> studentsByTeacher = studentRepository.findAllByTeacherId(teacher.get().getId());

        return studentsByTeacher.stream()
                .map(obj -> modelMapper.map(obj, RequestStudentDTO.class))
                .toList();
    }

    @Transactional
    public void deleteStudentByPhone(String phoneNumber) {
        studentRepository.deleteByPhone(phoneNumber);
    }

    public Optional<Student> findStudentByPhoneNumber(String phoneNumber) {
        return studentRepository.findByPhone(phoneNumber);
    }
}
