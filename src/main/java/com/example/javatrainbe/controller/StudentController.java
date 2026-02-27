package com.example.javatrainbe.controller;

import com.example.javatrainbe.dto.StudentRequestDto;
import com.example.javatrainbe.dto.StudentResponseDto;
import com.example.javatrainbe.service.StudentService;
import com.example.javatrainbe.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý Student APIs
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "API quản lý sinh viên")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * API lấy danh sách tất cả student
     */
    @GetMapping
    @Operation(summary = "Lấy danh sách sinh viên", description = "Lấy danh sách tất cả sinh viên")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudents() {
        List<StudentResponseDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sinh viên thành công", students));
    }

    /**
     * API lấy student theo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin sinh viên", description = "Lấy thông tin sinh viên theo ID")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(
            @Parameter(description = "ID sinh viên") @PathVariable Integer id) {
        StudentResponseDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin sinh viên thành công", student));
    }

    /**
     * API tạo student mới
     */
    @PostMapping
    @Operation(summary = "Tạo sinh viên mới", description = "Đăng ký sinh viên mới vào hệ thống")
    public ResponseEntity<ApiResponse<StudentResponseDto>> createStudent(
            @Valid @RequestBody StudentRequestDto studentRequestDto) {
        StudentResponseDto student = studentService.createStudent(studentRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Tạo sinh viên thành công", student));
    }

    /**
     * API cập nhật student
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật sinh viên", description = "Cập nhật thông tin sinh viên theo ID")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudent(
            @Parameter(description = "ID sinh viên") @PathVariable Integer id,
            @Valid @RequestBody StudentRequestDto studentRequestDto) {
        StudentResponseDto student = studentService.updateStudent(id, studentRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật sinh viên thành công", student));
    }

    /**
     * API xóa student
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa sinh viên", description = "Xóa sinh viên theo ID")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(
            @Parameter(description = "ID sinh viên") @PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa sinh viên thành công", null));
    }
}
