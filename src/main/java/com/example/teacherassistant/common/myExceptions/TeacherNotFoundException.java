package com.example.teacherassistant.common.myExceptions;

public class TeacherNotFoundException extends Exception{

    public TeacherNotFoundException(){
        super("Teacher Not Found");
    }

    public TeacherNotFoundException(String message){
        super(message);
    }

    public TeacherNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public TeacherNotFoundException(Throwable cause){
        super(cause);
    }
}
