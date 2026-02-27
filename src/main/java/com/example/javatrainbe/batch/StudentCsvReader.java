package com.example.javatrainbe.batch;

import com.example.javatrainbe.entity.StudentInfoWithStudent;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Batch Reader: Đọc dữ liệu từ bảng student JOIN student_info
 * Sử dụng JdbcCursorItemReader để streaming từng row, tránh OOM với dữ liệu lớn
 */
@Component
public class StudentCsvReader {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<StudentInfoWithStudent> studentCsvItemReader() {
        JdbcCursorItemReader<StudentInfoWithStudent> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("""
                SELECT s.student_id, s.student_name, s.student_code,
                       si.info_id, si.address, si.average_score, si.date_of_birth
                FROM student s
                LEFT JOIN student_info si ON s.student_id = si.student_id
                ORDER BY s.student_id
                """);
        reader.setRowMapper((rs, rowNum) -> {
            StudentInfoWithStudent item = new StudentInfoWithStudent();
            item.setStudentId(rs.getInt("student_id"));
            item.setStudentName(rs.getString("student_name"));
            item.setStudentCode(rs.getString("student_code"));
            item.setInfoId(rs.getObject("info_id") != null ? rs.getInt("info_id") : null);
            item.setAddress(rs.getString("address"));
            item.setAverageScore(rs.getObject("average_score") != null ? rs.getDouble("average_score") : null);
            item.setDateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null);
            return item;
        });
        reader.setFetchSize(100);
        return reader;
    }
}
