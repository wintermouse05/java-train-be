package com.example.javatrainbe.service;

import com.example.javatrainbe.dao.StudentDao;
import com.example.javatrainbe.dao.StudentInfoDao;
import com.example.javatrainbe.dto.StudentRequestDto;
import com.example.javatrainbe.dto.StudentResponseDto;
import com.example.javatrainbe.entity.Student;
import com.example.javatrainbe.entity.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service xử lý business logic cho Student
 */
@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentInfoDao studentInfoDao;

    /**
     * Lấy danh sách tất cả student
     */
    public List<StudentResponseDto> getAllStudents() {
        List<Student> students = studentDao.selectAll();
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy student theo ID
     */
    public StudentResponseDto getStudentById(Integer studentId) {
        Student student = studentDao.selectById(studentId);
        if (student == null) {
            throw new RuntimeException("Student không tồn tại với ID: " + studentId);
        }
        return convertToResponse(student);
    }

    /**
     * Tạo student mới
     */
    public StudentResponseDto createStudent(StudentRequestDto requestDto) {
        // Kiểm tra student code đã tồn tại chưa
        if (studentDao.findByStudentCode(requestDto.getStudentCode()).isPresent()) {
            throw new RuntimeException("Mã sinh viên đã tồn tại: " + requestDto.getStudentCode());
        }

        // Tạo và lưu Student entity (immutable - dùng constructor)
        Student student = new Student(null, requestDto.getStudentName(), requestDto.getStudentCode());
        Student savedStudent = studentDao.insert(student).getEntity();

        // Tạo và lưu StudentInfo
        StudentInfo studentInfo = new StudentInfo(
                null, savedStudent.getStudentId(),
                requestDto.getAddress(), requestDto.getAverageScore(), requestDto.getDateOfBirth());
        studentInfoDao.insert(studentInfo);

        return convertToResponse(savedStudent);
    }

    /**
     * Cập nhật student
     */
    public StudentResponseDto updateStudent(Integer studentId, StudentRequestDto requestDto) {
        // Kiểm tra student có tồn tại không
        Student student = studentDao.selectById(studentId);
        if (student == null) {
            throw new RuntimeException("Student không tồn tại với ID: " + studentId);
        }

        // Cập nhật Student (immutable - tạo entity mới)
        Student updatedStudent = new Student(student.getStudentId(),
                requestDto.getStudentName(), requestDto.getStudentCode());
        studentDao.update(updatedStudent);

        // Cập nhật StudentInfo
        Optional<StudentInfo> studentInfoOpt = studentInfoDao.findByStudentId(studentId);
        if (studentInfoOpt.isPresent()) {
            StudentInfo existing = studentInfoOpt.get();
            StudentInfo updatedInfo = new StudentInfo(
                    existing.getInfoId(), studentId,
                    requestDto.getAddress(), requestDto.getAverageScore(), requestDto.getDateOfBirth());
            studentInfoDao.update(updatedInfo);
        } else {
            // Tạo mới StudentInfo nếu chưa có
            StudentInfo newInfo = new StudentInfo(
                    null, studentId,
                    requestDto.getAddress(), requestDto.getAverageScore(), requestDto.getDateOfBirth());
            studentInfoDao.insert(newInfo);
        }

        return convertToResponse(updatedStudent);
    }

    /**
     * Xóa student
     */
    public void deleteStudent(Integer studentId) {
        Student student = studentDao.selectById(studentId);
        if (student == null) {
            throw new RuntimeException("Student không tồn tại với ID: " + studentId);
        }

        // Xóa StudentInfo trước (do foreign key constraint)
        Optional<StudentInfo> studentInfoOpt = studentInfoDao.findByStudentId(studentId);
        studentInfoOpt.ifPresent(studentInfo -> studentInfoDao.delete(studentInfo));

        // Xóa Student
        studentDao.delete(student);
    }

    /**
     * Convert Student entity sang StudentResponseDto
     */
    private StudentResponseDto convertToResponse(Student student) {
        StudentResponseDto response = new StudentResponseDto();
        response.setStudentId(student.getStudentId());
        response.setStudentName(student.getStudentName());
        response.setStudentCode(student.getStudentCode());

        // Lấy thông tin từ StudentInfo
        Optional<StudentInfo> studentInfoOpt = studentInfoDao.findByStudentId(student.getStudentId());
        if (studentInfoOpt.isPresent()) {
            StudentInfo studentInfo = studentInfoOpt.get();
            response.setAddress(studentInfo.getAddress());
            response.setAverageScore(studentInfo.getAverageScore());
            response.setDateOfBirth(studentInfo.getDateOfBirth());
        }

        return response;
    }
}
