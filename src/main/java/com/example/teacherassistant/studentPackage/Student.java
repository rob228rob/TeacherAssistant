package com.example.teacherassistant.studentPackage;

import com.example.teacherassistant.paymentInfoPackage.PaymentInfo;
import com.example.teacherassistant.imagePackage.StudentImage;
import com.example.teacherassistant.teacherPackage.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "students")
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "description")
    private String purposeDescription;

    @Column(name = "email", nullable = true)
    private String email;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_info_id", referencedColumnName = "id")
    private PaymentInfo paymentInfo;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "images", nullable = true)
    private List<StudentImage> studentImages;
}
