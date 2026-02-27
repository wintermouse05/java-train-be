package com.example.javatrainbe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 * Doma Entity cho bảng student
 */
@Entity(immutable = true)
@Table(name = "student")
@Data
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private final Integer studentId;

    @Column(name = "student_name")
    private final String studentName;

    @Column(name = "student_code")
    private final String studentCode;
}
