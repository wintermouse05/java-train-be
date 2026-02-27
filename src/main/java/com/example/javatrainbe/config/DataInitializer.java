package com.example.javatrainbe.config;

import com.example.javatrainbe.dao.StudentDao;
import com.example.javatrainbe.dao.StudentInfoDao;
import com.example.javatrainbe.dao.UserDao;
import com.example.javatrainbe.entity.Student;
import com.example.javatrainbe.entity.StudentInfo;
import com.example.javatrainbe.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Khởi tạo dữ liệu mẫu khi ứng dụng khởi động
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentInfoDao studentInfoDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo user mẫu nếu chưa có
        if (userDao.selectAll().isEmpty()) {
            createSampleUsers();
        }

        // Tạo student mẫu nếu chưa có
        if (studentDao.selectAll().isEmpty()) {
            createSampleStudents();
        }
    }

    private void createSampleUsers() {
        User admin = new User("admin", passwordEncoder.encode("123456"));
        userDao.insert(admin);

        User user = new User("user", passwordEncoder.encode("123456"));
        userDao.insert(user);
    }

    private void createSampleStudents() {
        // Student 1
        Student student1 = new Student(null, "Nguyễn Văn A", "SV001");
        Student saved1 = studentDao.insert(student1).getEntity();

        StudentInfo info1 = new StudentInfo(null, saved1.getStudentId(),
                "123 Đường ABC, Quận 1, TP.HCM", 8.5, LocalDate.of(2000, 1, 15));
        studentInfoDao.insert(info1);

        // Student 2
        Student student2 = new Student(null, "Trần Thị B", "SV002");
        Student saved2 = studentDao.insert(student2).getEntity();

        StudentInfo info2 = new StudentInfo(null, saved2.getStudentId(),
                "456 Đường XYZ, Quận 2, TP.HCM", 9.0, LocalDate.of(2001, 3, 20));
        studentInfoDao.insert(info2);

        // Student 3
        Student student3 = new Student(null, "Lê Văn C", "SV003");
        Student saved3 = studentDao.insert(student3).getEntity();

        StudentInfo info3 = new StudentInfo(null, saved3.getStudentId(),
                "789 Đường DEF, Quận 3, TP.HCM", 7.8, LocalDate.of(1999, 7, 10));
        studentInfoDao.insert(info3);
    }
}
