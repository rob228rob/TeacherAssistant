package com.example.teacherassistant.lessonsPackage;

public class LessonsNotFoundException extends RuntimeException {
    public LessonsNotFoundException() {
        super("Lessons Not Found");
    }

    public LessonsNotFoundException(String message) {
        super(message);
    }

    public LessonsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
