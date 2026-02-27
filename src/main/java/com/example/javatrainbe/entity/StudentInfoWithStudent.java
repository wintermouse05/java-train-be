package com.example.javatrainbe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * POJO cho batch CSV export (JOIN student + student_info)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfoWithStudent {
    private Integer studentId;
    private String studentName;
    private String studentCode;
    private Integer infoId;
    private String address;
    private Double averageScore;
    private LocalDate dateOfBirth;
}
