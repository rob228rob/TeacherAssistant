package com.example.teacherassistant.controllers;

import com.example.teacherassistant.dtos.ErrorHandler;
import com.example.teacherassistant.entities.StudentImage;
import com.example.teacherassistant.myExceptions.ImageNotFoundException;
import com.example.teacherassistant.myExceptions.StudentNotFoundException;
import com.example.teacherassistant.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health Check OK");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam(name = "phone") String phoneNumber, @RequestParam("file1") MultipartFile file) {
        try {
            return new ResponseEntity<>(imageService.saveImage(file, phoneNumber), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (StudentNotFoundException e) {
            return  new ResponseEntity<>(new ErrorHandler(404, "student by phone not found"),HttpStatus.NOT_FOUND);
        }
    }


    //TODO: !!!!!!!!!!
    @GetMapping(value = "/download/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        try {
            StudentImage foundFile = imageService.findImageById(id);
            Resource resource = imageService.download(foundFile.getFileKey());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + foundFile.getFileName())
                    .body(resource);
        } catch (IOException | ImageNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
