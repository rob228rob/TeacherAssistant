package com.example.teacherassistant.common.dtos;

import com.example.teacherassistant.common.myExceptions.InvalidPaymentInfoDataException;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDTO {
    private double pricePerHour;

    private double minutesPerLesson;

    private int lessonsPerWeek;

    public PaymentInfoDTO validatePaymentInfoDTO() throws InvalidPaymentInfoDataException {
        this
                .validatePricePerHour()
                .validateMinutesPerLessons()
                .validateLessonsPerWeek();

        return this;
    }

    private PaymentInfoDTO validateLessonsPerWeek() throws InvalidPaymentInfoDataException {
        if (lessonsPerWeek < 1 || lessonsPerWeek > 7) {
            throw new InvalidPaymentInfoDataException("Invalid number of lessons per week. Must be from 1 to 7");
        }

        return this;
    }

    private PaymentInfoDTO validateMinutesPerLessons() throws InvalidPaymentInfoDataException {
        if (minutesPerLesson < 1 || minutesPerLesson > 180 || minutesPerLesson % 15 != 0) {
            throw new InvalidPaymentInfoDataException("minutes per lessons must be from 15 to 180");
        }

        return this;
    }

    private PaymentInfoDTO validatePricePerHour() throws InvalidPaymentInfoDataException {
        if (pricePerHour < 50) {
            throw new InvalidPaymentInfoDataException("Price per hour must be not less than 50");
        }

        return this;
    }
}
