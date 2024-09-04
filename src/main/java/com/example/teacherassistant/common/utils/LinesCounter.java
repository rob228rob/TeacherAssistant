package com.example.teacherassistant.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LinesCounter {

    public static void main(String[] args) {

        String absDirPath = "src/main";

        Path startPath = Paths.get(absDirPath).toAbsolutePath();
        System.out.println("Searching in directory: " + startPath);

        try (Stream<Path> paths = Files.walk(startPath)) {
            long totalLines = paths
                    .filter(Files::isRegularFile) // Фильтруем только файлы
                    .filter(path -> {
                        String fileName = path.toString().toLowerCase();
                        return fileName.endsWith(".java")
                                || fileName.endsWith(".css")
                                || fileName.endsWith(".html")
                                || fileName.endsWith(".js");
                    })
                    .peek(path -> System.out.println("Processing file: " + path))
                    .mapToLong(LinesCounter::countLinesInFile)
                    .sum();

            System.out.println("Total number of lines in all files: " + totalLines);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static long countLinesInFile(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            long linesCount = reader.lines().count();
            System.out.println("Reading file: " + filePath + "; total line: " + linesCount);
            return linesCount;
        } catch (IOException e) {
            System.out.println("Error reading file " + filePath + ": " + e.getMessage());
            return 0;
        }
    }
}
