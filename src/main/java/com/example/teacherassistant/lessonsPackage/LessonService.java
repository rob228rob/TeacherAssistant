package com.example.teacherassistant.lessonsPackage;

import com.example.teacherassistant.common.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.studentPackage.Student;
import com.example.teacherassistant.studentPackage.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    private final StudentService studentService;
    private final ModelMapper modelMapper;

    public LessonResponseDTO addNewLessonByStudent(LessonRequestDTO lessonDTO) {
        long id = lessonDTO.getStudentId();
        Lesson lesson = modelMapper.map(lessonDTO, Lesson.class);
        lesson.updateStatus();
        return modelMapper.map(
                lessonRepository.save(lesson), LessonResponseDTO.class);
    }

     public LessonResponseDTO getLessonById(Long lessonId) {
        return modelMapper.map(lessonRepository.findById(lessonId), LessonResponseDTO.class);
     }

    public Collection<LessonResponseDTO> getAllLessonsByStudentId(long id) throws StudentNotFoundException {
        if (!studentService.checkIfStudentExistById(id)) {
            throw new StudentNotFoundException("Student with id " + id + " not found");
        }

        Collection<Lesson> allLessonsByStudentId = lessonRepository.getAllLessonsByStudentId(id);
        if (allLessonsByStudentId.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by student id " + id + " not found");
        }

        return allLessonsByStudentId.stream()
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();
    }

    public List<LessonResponseDTO> getAllLessonsByStudentIdOrderByTime(long id) throws StudentNotFoundException {
        if (!studentService.checkIfStudentExistById(id)) {
            throw new StudentNotFoundException("Student with id " + id + " not found");
        }
        Collection<Lesson> lessonsByStudent = lessonRepository.getAllLessonsByStudentIdOrderByTimeExpired(id);
        if (lessonsByStudent.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by student id " + id + " not found");
        }

        return lessonsByStudent.stream()
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();
    }
}
