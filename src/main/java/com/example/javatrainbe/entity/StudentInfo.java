package com.example.javatrainbe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.time.LocalDate;

/**
 * Doma Entity cho bảng student_info
 */
@Entity(immutable = true)
@Table(name = "student_info")
@Data
@AllArgsConstructor
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private final Integer infoId;

    @Column(name = "student_id")
    private final Integer studentId;

    @Column(name = "address")
    private final String address;

    @Column(name = "average_score")
    private final Double averageScore;

    @Column(name = "date_of_birth")
    private final LocalDate dateOfBirth;
    
}
