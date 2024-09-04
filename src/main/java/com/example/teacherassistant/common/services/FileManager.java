package com.example.teacherassistant.common.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileManager {

    String saveFile(MultipartFile file) throws IOException;

    Resource loadFile(String fileKey) throws IOException;

    void deleteFile(String fileKey) throws IOException;

    String getFilePath(String fileKey);
}
