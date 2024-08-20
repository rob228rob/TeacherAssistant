package com.example.teacherassistant.services;

import com.example.teacherassistant.dtos.TeacherDTO;
import com.example.teacherassistant.entities.Student;
import com.example.teacherassistant.entities.Teacher;
import com.example.teacherassistant.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    public Teacher findTeacherById(Long id) throws TeacherNotFoundException {
        Optional<Teacher> teacher = teacherRepository.findById(id);

        return teacher.orElseThrow(() -> new TeacherNotFoundException("teacher with id: " + id + " not found."));
    }

    public Teacher findTeacherByPhone(String phone) throws TeacherNotFoundException {
        Optional<Teacher> teacher = teacherRepository.findByPhoneNumber(phone);

        return teacher.orElseThrow(() -> new TeacherNotFoundException("teacher with phone: " + phone + " not found."));
    }

    public Collection<Student> findStudentsByTeacherId(Long id) throws TeacherNotFoundException {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher with id: " + id + " not found."));

        return teacher.getStudents();
    }

    public Teacher saveTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    public boolean updateTeacher(long id, TeacherDTO teacherDTO) {
        if (teacherRepository.existsByPhoneNumber(teacherDTO.getPhoneNumber())) {
            return false;
        }
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        teacher.setId(id);
        teacher.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
        teacherRepository.save(teacher);

        return true;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return teacherRepository.existsByPhoneNumber(phoneNumber);
    }

    public Optional<Teacher> getTeacherById(long id) {
        return teacherRepository.findById(id);
    }

    public boolean deleteTeacherById(long id) {
        teacherRepository.deleteById(id);

        return true;
    }
}
