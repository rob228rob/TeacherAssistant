package com.example.teacherassistant.common.myExceptions;

public class InvalidTeacherCredentials extends Exception {
    public InvalidTeacherCredentials(String invalidAccessToThisEndpoint) {
        super(invalidAccessToThisEndpoint);
    }

    public InvalidTeacherCredentials(String invalidAccessToThisEndpoint, Throwable cause) {
        super(invalidAccessToThisEndpoint, cause);
    }

    public InvalidTeacherCredentials() {
        super("Invalid user credentials");
    }
}
