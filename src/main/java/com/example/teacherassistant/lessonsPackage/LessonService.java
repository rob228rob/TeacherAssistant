package com.example.teacherassistant.lessonsPackage;

import com.example.teacherassistant.common.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.studentPackage.StudentService;
import com.example.teacherassistant.teacherPackage.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    private final StudentService studentService;
    private final ModelMapper modelMapper;
    private final RestTemplateAutoConfiguration restTemplateAutoConfiguration;
    private final TeacherService teacherService;

    public LessonResponseDTO addNewLessonByStudent(LessonRequestDTO lessonDTO) throws StudentNotFoundException {
        long studentId = lessonDTO.getStudentId();
        Lesson lesson = modelMapper.map(lessonDTO, Lesson.class);
        lesson.setTeacher(studentService.findTeacherByStudentId(studentId));
        lesson.updateStatus();
        LessonResponseDTO lessonResponseDTO = modelMapper.map(lessonRepository.save(lesson), LessonResponseDTO.class);
        lessonResponseDTO.setStudentId(studentId);

        return lessonResponseDTO;
    }

     public LessonResponseDTO getLessonById(Long lessonId) {
        return modelMapper.map(lessonRepository.findById(lessonId), LessonResponseDTO.class);
     }

    public Collection<LessonResponseDTO> getAllLessonsByStudentId(long id) throws StudentNotFoundException, LessonsNotFoundException {
        if (!studentService.checkIfStudentExistById(id)) {
            throw new StudentNotFoundException("Student with id " + id + " not found");
        }

        Collection<Lesson> allLessonsByStudentId = lessonRepository.getAllLessonsByStudentId(id);
        if (allLessonsByStudentId.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by student id " + id + " not found");
        }

        return allLessonsByStudentId.stream()
                .peek(Lesson::updateStatus)
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();
    }

    public List<LessonResponseDTO> getAllLessonsByStudentIdOrderByTime(long id) throws LessonsNotFoundException, StudentNotFoundException {
        if (!studentService.checkIfStudentExistById(id)) {
            throw new StudentNotFoundException("Student with id " + id + " not found");
        }
        Collection<Lesson> lessonsByStudent = lessonRepository.getAllLessonsByStudentIdOrderByTimeExpired(id);
        if (lessonsByStudent.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by student id " + id + " not found");
        }

        return lessonsByStudent.stream()
                .peek(Lesson::updateStatus)
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();
    }

    public void updateLessonStatusToCanceled(long lessonId) throws LessonsNotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonsNotFoundException("Lesson with id " + lessonId + " not found"));
        lesson.cancelLesson();
        lessonRepository.save(lesson);
    }

    public List<LessonResponseDTO> getAllLessonsByTeacherId(long teacherId) throws LessonsNotFoundException {
        Collection<Lesson> allLessonsByTeacherId = lessonRepository.getAllLessonsByTeacherId(teacherId);

        if (allLessonsByTeacherId.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by teacher id " + teacherId + " not found");
        }

        return allLessonsByTeacherId.stream()
                .peek(Lesson::updateStatus)
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();
    }

    @Transactional
    public void hideLessonDisplayingById(long lessonId) throws LessonsNotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonsNotFoundException("Lesson with id " + lessonId + " not found"));
        lesson.setHidden(true);
        lessonRepository.save(lesson);
    }

    public List<LessonResponseDTO> getAllHiddenLessonsByTeacherId(long teacherId) throws LessonsNotFoundException {
        Collection<Lesson> allLessonsByTeacherId = lessonRepository.getAllLessonsByTeacherId(teacherId);

        if (allLessonsByTeacherId.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by teacher id " + teacherId + " not found");
        }

        return allLessonsByTeacherId.stream()
                .peek(Lesson::updateStatus)
                .filter(Lesson::isHidden)
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();
    }
}
