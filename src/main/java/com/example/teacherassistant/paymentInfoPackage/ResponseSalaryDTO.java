package com.example.teacherassistant.paymentInfoPackage;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseSalaryDTO {

    private double monthlySalary;

    private double weeklySalary;

    private int lessonsPerMonth;

    private BigDecimal averageSalaryPerLesson;

    public void updateAverageSalaryPerLesson() {
        if (averageSalaryPerLesson != null && lessonsPerMonth > 0) {
            averageSalaryPerLesson = BigDecimal.valueOf(monthlySalary / lessonsPerMonth);
        } else if (lessonsPerMonth > 0) {
            averageSalaryPerLesson = new BigDecimal(monthlySalary / lessonsPerMonth);
        }
    }

    public ResponseSalaryDTO(double monthlySalary, double weeklySalary, int lessonsPerMonth) {
        this.monthlySalary = monthlySalary;
        this.weeklySalary = weeklySalary;
        this.lessonsPerMonth = lessonsPerMonth;
        this.averageSalaryPerLesson = BigDecimal.ZERO;
    }
}
