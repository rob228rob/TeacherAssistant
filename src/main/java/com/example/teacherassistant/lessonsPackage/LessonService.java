package com.example.teacherassistant.lessonsPackage;

import com.example.teacherassistant.common.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.studentPackage.StudentService;
import com.example.teacherassistant.teacherPackage.Teacher;
import com.example.teacherassistant.teacherPackage.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
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

    private final TeacherService teacherService;

   // @CacheEvict(value = "lessonsByTeacherId", key = "'lessons_by_teacher_' + #teacher.id")
    public LessonResponseDTO addNewLessonByStudent(LessonRequestDTO lessonDTO) throws StudentNotFoundException {
        long studentId = lessonDTO.getStudentId();
        Lesson lesson = modelMapper.map(lessonDTO, Lesson.class);
        Teacher teacher = studentService.findTeacherByStudentId(studentId);
        lesson.setTeacher(teacher);
        lesson.updateStatus();
        LessonResponseDTO lessonResponseDTO = modelMapper.map(lessonRepository.save(lesson), LessonResponseDTO.class);
        lessonResponseDTO.setStudentId(studentId);

        return lessonResponseDTO;
    }

    public void updateLessonStatusToCanceled(long lessonId) throws LessonsNotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonsNotFoundException("Lesson with id " + lessonId + " not found"));
        lesson.cancelLesson();
        lessonRepository.save(lesson);
    }

    public LessonResponseDTO getLessonById(Long lessonId) {
        return modelMapper.map(lessonRepository.findById(lessonId), LessonResponseDTO.class);
    }

   // @Cacheable(value = "lessonsByStudentId", key = "'all_lessons_by_student_' + #studentId")
    public Collection<LessonResponseDTO> getAllLessonsByStudentId(long studentId) throws StudentNotFoundException, LessonsNotFoundException {
        if (!studentService.checkIfStudentExistById(studentId)) {
            throw new StudentNotFoundException("Student with studentId " + studentId + " not found");
        }

        Collection<Lesson> allLessonsByStudentId = lessonRepository.getAllLessonsByStudentId(studentId);
        if (allLessonsByStudentId.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by student studentId " + studentId + " not found");
        }

        return allLessonsByStudentId.stream()
                .peek(Lesson::updateStatus)
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();
    }

   // @Cacheable(value = "lessonsSortedByStudentId", key = "'sorted_lessons_by_student_' + #studentId")
    public List<LessonResponseDTO> getAllLessonsByStudentIdOrderByTime(long studentId) throws LessonsNotFoundException, StudentNotFoundException {
        if (!studentService.checkIfStudentExistById(studentId)) {
            throw new StudentNotFoundException("Student with studentId " + studentId + " not found");
        }
        Collection<Lesson> lessonsByStudent = lessonRepository.getAllLessonsByStudentIdOrderByTimeExpired(studentId);
        if (lessonsByStudent.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by student studentId " + studentId + " not found");
        }

        return lessonsByStudent.stream()
                .peek(Lesson::updateStatus)
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();
    }

    //@Cacheable(value = "lessonsByTeacherId", key = "'lessons_by_teacher_' + #teacherId")
    public List<LessonResponseDTO> getAllLessonsByTeacherId(long teacherId) throws LessonsNotFoundException {
        Collection<Lesson> allLessonsByTeacherId = lessonRepository.getAllLessonsByTeacherId(teacherId);

        if (allLessonsByTeacherId.isEmpty()) {
            throw new LessonsNotFoundException("Lessons by teacher id " + teacherId + " not found");
        }

        List<LessonResponseDTO> lessonsDTO = allLessonsByTeacherId.stream()
                .peek(Lesson::updateStatus)
                .map(x -> modelMapper.map(x, LessonResponseDTO.class))
                .toList();

        return lessonsDTO;
    }

    @Transactional
    public void hideLessonDisplayingById(long lessonId) throws LessonsNotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonsNotFoundException("Lesson with id " + lessonId + " not found"));
        lesson.setHidden(true);
        lessonRepository.save(lesson);
    }

    @Transactional
    public void unHideLessonDisplayingById(long lessonId) throws LessonsNotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new LessonsNotFoundException("Lesson with id " + lessonId + " not found"));
        lesson.setHidden(false);
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
