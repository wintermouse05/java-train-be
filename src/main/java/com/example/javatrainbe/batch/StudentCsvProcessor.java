package com.example.javatrainbe.batch;

import com.example.javatrainbe.entity.StudentInfoWithStudent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Batch Processor: Chuyển đổi StudentInfoWithStudent sang String array cho CSV
 */
@Slf4j
@Component
public class StudentCsvProcessor implements ItemProcessor<StudentInfoWithStudent, String[]> {

    @Override
    public String[] process(StudentInfoWithStudent item) {
        log.debug("Processing student: {} - {}", item.getStudentId(), item.getStudentName());

        return new String[]{
                String.valueOf(item.getStudentId()),
                item.getStudentName() != null ? item.getStudentName() : "",
                item.getStudentCode() != null ? item.getStudentCode() : "",
                item.getAddress() != null ? item.getAddress() : "",
                item.getAverageScore() != null ? String.valueOf(item.getAverageScore()) : "",
                item.getDateOfBirth() != null ? item.getDateOfBirth().toString() : ""
        };
    }
}
