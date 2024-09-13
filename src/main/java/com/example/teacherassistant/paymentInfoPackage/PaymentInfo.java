package com.example.teacherassistant.paymentInfoPackage;

import com.example.teacherassistant.studentPackage.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Table(name = "student_payment")
@Entity(name = "student_payment")
@NoArgsConstructor
public class PaymentInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne()
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @Column(name = "price_per_hour", nullable = false)
    private double pricePerHour;

    @Column(name = "minutes_per_lesson")
    private double minutesPerLesson;

    @Column(name = "lessons_per_week")
    private int lessonsPerWeek;

    public double calculatePricePerMonth() {
        //preTODO: need to up calc method with calendar

        return calculatePricePerWeek() * 4;
    }

    public double calculatePricePerWeek() {
        return pricePerHour * (minutesPerLesson / 60.0) * lessonsPerWeek;
    }

    public int lessonsPerMonth() {
        return lessonsPerWeek * 4;
    }

}
