package com.example.teacherassistant.common.myExceptions;

public class InvalidStudentDataException extends Exception {
    public InvalidStudentDataException(String message) {
      super(message);
    }

    public InvalidStudentDataException(String message, Throwable cause) {
      super(message, cause);
    }

    public InvalidStudentDataException(Throwable cause) {
      super(cause);
    }

}
