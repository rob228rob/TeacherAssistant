package com.example.teacherassistant.common.myExceptions;

public class StudentNotFoundException extends Exception{

    public StudentNotFoundException(){
        super("Student Not Found");
    }

    public StudentNotFoundException(String message){
        super(message);
    }

    public StudentNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public StudentNotFoundException(Throwable cause){
        super(cause);
    }
}
