package com.example.teacherassistant.common.myExceptions;

public class StudentAlreadyExistException extends Exception {

    public StudentAlreadyExistException() {
        super("Student already exists");
    }

    public StudentAlreadyExistException(String message) {
        super(message);
    }
}
