package com.example.teacherassistant.services;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class FilesystemFileManager implements FileManager {

    @Value("${app.image-storage.filesystem.path}")
    private String pathToStorage;

    private Path imageStoragePath;

    @PostConstruct
    public void init() {
        try {
            log.info("Initializing FilesystemFileManager with storage path: {}", pathToStorage);

            if (pathToStorage == null || pathToStorage.isEmpty()) {
                throw new IllegalArgumentException("The path to storage is not configured properly");
            }

            imageStoragePath = Paths.get(pathToStorage, "image-storage");

            if (!Files.exists(imageStoragePath)) {
                Files.createDirectories(imageStoragePath);
                log.info("Directory created successfully: {}", imageStoragePath.toAbsolutePath());
            } else {
                log.info("Directory already exists: {}", imageStoragePath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Failed to create directory: {}", imageStoragePath != null ? imageStoragePath.toAbsolutePath() : "Unknown path", e);
            throw new RuntimeException("Could not initialize file manager", e);  // Завершаем инициализацию с ошибкой
        }
    }

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        String fileKey = UUID.randomUUID().toString();
        Path destinationFile = imageStoragePath.resolve(fileKey);

        try (FileOutputStream fos = new FileOutputStream(destinationFile.toFile())) {
            fos.write(file.getBytes());
            log.info("File saved successfully: {}", destinationFile.toAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to save file: {}", destinationFile.toAbsolutePath(), e);
            throw e;
        }

        return fileKey;
    }

    @Override
    public Resource loadFile(String fileKey) throws IOException {
        Path filePath = imageStoragePath.resolve(fileKey);
        File file = filePath.toFile();

        if (file.exists()) {
            return new UrlResource(file.toURI());
        } else {
            log.error("File not found: {}", filePath.toAbsolutePath());
            throw new IOException("File not found: " + filePath.toAbsolutePath());
        }
    }

    @Override
    public void deleteFile(String fileKey) throws IOException {
        Path filePath = imageStoragePath.resolve(fileKey);
        File file = filePath.toFile();

        if (file.exists() && file.delete()) {
            log.info("File deleted successfully: {}", filePath.toAbsolutePath());
        } else {
            log.error("Failed to delete file or file does not exist: {}", filePath.toAbsolutePath());
            throw new IOException("Failed to delete file: " + filePath.toAbsolutePath());
        }
    }

    @Override
    public String getFilePath(String fileKey) {
        return Paths.get(pathToStorage, "image-storage", fileKey).toString();
    }
}