package com.example.teacherassistant.studentPackage;

import com.example.teacherassistant.common.myExceptions.InvalidTeacherCredentials;
import com.example.teacherassistant.imagePackage.StudentImage;
import com.example.teacherassistant.teacherPackage.Teacher;
import com.example.teacherassistant.common.myExceptions.InvalidStudentDataException;
import com.example.teacherassistant.common.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.common.myExceptions.TeacherNotFoundException;
import com.example.teacherassistant.teacherPackage.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

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

    public Collection<Student> getCollectionStudentsByTeacherPhone(String teacherPhone) {
        return studentRepository.findAllByTeacherPhoneNumber(teacherPhone);
    }

    public List<ResponseStudentDTO> getAllStudentsByTeacherPhone(String phone) throws TeacherNotFoundException {
        Optional<Teacher> teacher = teacherService.findTeacherByPhone(phone);
        if (teacher.isEmpty()) {
            throw new TeacherNotFoundException("teacher with phone: " + phone + " does not exist");
        }

        Collection<Student> studentsByTeacher = studentRepository.findAllByTeacherId(teacher.get().getId());

        return studentsByTeacher.stream()
                .map(student -> {
                    var reqStudentDTO = modelMapper.map(student, ResponseStudentDTO.class);
                    List<Long> imageIds = student.getStudentImages().stream().map(StudentImage::getId).toList();
                    reqStudentDTO.setImageIds(imageIds);

                    return reqStudentDTO;
                })
                .toList();
    }

    @Transactional(rollbackOn = {StudentNotFoundException.class, TeacherNotFoundException.class, InvalidStudentDataException.class})
    public void deleteStudentByPhone(String phoneNumber, String teacherPhone) throws TeacherNotFoundException, StudentNotFoundException, InvalidTeacherCredentials {
        var teacher = teacherService.findTeacherByPhone(teacherPhone);
        if (teacher.isEmpty()) {
            throw new TeacherNotFoundException("Teacher with phone: " + teacherPhone + "not found");
        }
        var studentToDelete = getStudentByPhoneNumber(phoneNumber);
        if (studentToDelete.isEmpty()) {
            throw  new StudentNotFoundException("Student with phone: " + phoneNumber + " does not exist");
        }

        if (!teacher.get().getId().equals(studentToDelete.get().getTeacher().getId())) {
            throw new InvalidTeacherCredentials("Invalid access to this endpoint");
        }

        studentRepository.deleteByPhone(phoneNumber);
    }

    public Optional<Student> findStudentByPhoneNumber(String studentPhoneNumber, String teacherPhoneNumber) {
        return studentRepository.findByPhoneAndTeacherPhoneNumber(studentPhoneNumber, teacherPhoneNumber);
    }

    @Transactional
    public void updateStudentPartly(RequestStudentDTO requestStudentDTO, String phoneNumber) throws StudentNotFoundException, InvalidStudentDataException {
        requestStudentDTO.partlyValidateStudentDTO();

        Optional<Student> optionalStudent = studentRepository.findByPhone(phoneNumber);
        if (optionalStudent.isEmpty()) {
            throw new StudentNotFoundException("Student with phone number: " + phoneNumber + " not found.");
        }

        if (requestStudentDTO.getEmail() != null) {
            System.out.println("current email for upd: " + requestStudentDTO.getEmail());
        }

        Student student = optionalStudent.get();
        updateFieldIfNotNull(requestStudentDTO.getName(), student::setName);
        updateFieldIfNotNull(requestStudentDTO.getSurname(), student::setSurname);
        updateFieldIfNotNull(requestStudentDTO.getEmail(), student::setEmail);
        updateFieldIfNotNull(requestStudentDTO.getPhone(), student::setPhone);
        updateFieldIfNotNull(requestStudentDTO.getPurposeDescription(), student::setPurposeDescription);
        updateFieldIfNotZero(requestStudentDTO.getGrade(), student::setGrade);

        studentRepository.save(student);
    }

    private <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private void updateFieldIfNotZero(int value, IntConsumer setter) {
        if (value != 0) {
            setter.accept(value);
        }
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() == 11;
    }

    public Optional<Student> getStudentByPhoneNumber(String studentPhoneNumber) {
        return studentRepository.findByPhone(studentPhoneNumber);
    }

    public boolean checkIfStudentExistById(long id) {
        return studentRepository.existsById(id);
    }

    public Teacher findTeacherByStudentId(long studentId) throws StudentNotFoundException {
        return studentRepository.findTeacherByStudentId(studentId).orElseThrow(() -> new StudentNotFoundException("Student with id: " + studentId + " not found"));
    }
}
