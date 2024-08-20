package com.example.teacherassistant.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorHandler {
    private int errorCode;

    private String errorMessage;

    private Date timestamp;

    public ErrorHandler(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = new Date();
    }
}