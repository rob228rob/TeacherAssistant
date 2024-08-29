package com.example.teacherassistant.myExceptions;

public class InvalidPaymentInfoDataException extends Throwable {
    public InvalidPaymentInfoDataException(String s) {
        super(s);
    }

    public InvalidPaymentInfoDataException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidPaymentInfoDataException(Throwable throwable) {
        super(throwable);
    }

    public InvalidPaymentInfoDataException() {
        super("Invalid Payment Info");
    }
}
