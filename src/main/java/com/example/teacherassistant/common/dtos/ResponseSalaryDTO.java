package com.example.teacherassistant.common.dtos;

import lombok.*;
import org.hibernate.annotations.Bag;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseSalaryDTO {

    private double monthlySalary;

    private double weeklySalary;

    private int lessonsPerMonth;
}
