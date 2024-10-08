package com.example.teacherassistant.imagePackage;

import com.example.teacherassistant.common.services.FileManager;
import com.example.teacherassistant.studentPackage.Student;
import com.example.teacherassistant.common.myExceptions.ImageNotFoundException;
import com.example.teacherassistant.common.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.studentPackage.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final FileManager fileManager;

    private final StudentService studentService;

    @Transactional(rollbackFor = {IOException.class, StudentNotFoundException.class})
    public StudentImage saveImage(MultipartFile file, String studentPhoneNumber, String teacherPhoneNumber) throws IOException, StudentNotFoundException {

        String fileKey = fileManager.saveFile(file);

        Student student = studentService.findStudentByPhoneNumber(studentPhoneNumber, teacherPhoneNumber)
                .orElseThrow(() -> new StudentNotFoundException("Student with phone number: " + studentPhoneNumber + " not found"));

        StudentImage studentImage = new StudentImage();
        studentImage.setFileKey(fileKey);
        studentImage.setFileName(file.getOriginalFilename());
        studentImage.setFilePath(fileManager.getFilePath(fileKey));
        studentImage.setMimeType(file.getContentType());
        studentImage.setSize(file.getSize());
        studentImage.setStudent(student);

        return imageRepository.save(studentImage);
    }

    public StudentImage getImage(String phoneNumber, String teacherPhone) throws StudentNotFoundException {
        Optional<Student> studentByPhoneNumber = studentService.findStudentByPhoneNumber(phoneNumber, teacherPhone);
        if (studentByPhoneNumber.isEmpty()) {
            throw new StudentNotFoundException("Student with phone number: " + phoneNumber + " not found");
        }

        return imageRepository.findById(studentByPhoneNumber.get().getId()).get();
    }

    @Transactional(rollbackFor = {IOException.class, ImageNotFoundException.class})
    public void deleteImageById(Long imageId) throws ImageNotFoundException, IOException {
        Optional<StudentImage> imageById = imageRepository.findById(imageId);
        if (imageById.isEmpty()) {
            throw new ImageNotFoundException("image with id: " + imageId + " not found");
        }

        fileManager.deleteFile(imageById.get().getFileKey());
        imageRepository.deleteById(imageId);
    }

    public Resource download(String fileKey) throws IOException {
        return fileManager.loadFile(fileKey);
    }

    public StudentImage findImageById(Long id) throws ImageNotFoundException {
        return imageRepository.findById(id).orElseThrow(
                () -> new ImageNotFoundException("Image with id: " + id + " not found")
        );
    }
}
