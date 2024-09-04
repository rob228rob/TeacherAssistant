package com.example.teacherassistant.studentPackage;

import com.example.teacherassistant.common.myExceptions.InvalidStudentDataException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestStudentDTO {

    private String name;

    private String surname;

    private String phone;

    private String email;

    private String purposeDescription;

    private int grade;

    public RequestStudentDTO partlyValidateStudentDTO() throws InvalidStudentDataException {
        if (grade != 0) {
            validateGrade();
        }

        if (name != null) {
            validateName();
        }

        if (surname != null) {
            validateSurname();
        }

        if (phone != null) {
            validatePhone();
        }

        if (email != null) {
            validateEmail();
        }

        if (purposeDescription != null) {
            validatePurposeDescription();
        }

        return this;
    }

    private RequestStudentDTO validatePurposeDescription() throws InvalidStudentDataException {
        if (purposeDescription == null) {
            throw new InvalidStudentDataException("Description is null");
        }

        if (purposeDescription.length() > 120) {
            throw new InvalidStudentDataException("Description must be less than 120 characters");
        }

        if (purposeDescription.length() <= 10) {
            throw new InvalidStudentDataException("Description must be more than 10 characters");
        }

        return this;
    }

    public RequestStudentDTO validateStudentDTO() throws InvalidStudentDataException {
        this
                .validateName()
                .validateSurname()
                .validateEmail()
                .validatePhone()
                .validateGrade();

        return this;
    }

    private RequestStudentDTO validatePhone() throws InvalidStudentDataException {
        if (phone.length() != 11 && phone.charAt(0) != '+') {
            throw new InvalidStudentDataException("phone number is incorrect");
        }

        return this;
    }

    private RequestStudentDTO validateEmail() throws InvalidStudentDataException {
        if (email == null || email.isEmpty()) {
            throw new InvalidStudentDataException("Email is required");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new InvalidStudentDataException("Invalid email format");
        }

        int atPosition = email.indexOf('@');
        int dotPosition = email.lastIndexOf('.');

        if (atPosition < 1 || dotPosition < atPosition + 2 || dotPosition + 2 >= email.length()) {
            throw new InvalidStudentDataException("Invalid email format");
        }

        return this;
    }

    private RequestStudentDTO validateGrade() throws InvalidStudentDataException {
        if (grade < 1 || grade > 11) {
            throw new InvalidStudentDataException("Age must be between 0 and 100");
        }

        return this;
    }

    private RequestStudentDTO validateName() throws InvalidStudentDataException {
        if (name == null || name.isEmpty()) {
            throw new InvalidStudentDataException("First name cannot be empty");
        }

        if (name.length() > 15 || name.length() < 3) {
            throw new InvalidStudentDataException("Name length must be between 3 and 15");
        }

        return this;
    }

    private RequestStudentDTO validateSurname() throws InvalidStudentDataException {
        if (surname == null || surname.isEmpty()) {
            throw new InvalidStudentDataException("Surname cannot be empty");
        }

        if (surname.length() < 3 || surname.length() > 15) {
            throw new InvalidStudentDataException("Surname length must be between 3 and 15");
        }

        return this;
    }
}
