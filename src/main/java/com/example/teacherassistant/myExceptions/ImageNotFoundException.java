package com.example.teacherassistant.myExceptions;

public class ImageNotFoundException extends Exception {
    public ImageNotFoundException(String s) {
    }

    public ImageNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ImageNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public ImageNotFoundException() {
        super();
    }
}
