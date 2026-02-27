package com.example.javatrainbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO cho response student
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response thông tin student")
public class StudentResponseDto {

    @Schema(description = "ID sinh viên", example = "1")
    private Integer studentId;

    @Schema(description = "Tên sinh viên", example = "Nguyễn Văn A")
    private String studentName;

    @Schema(description = "Mã sinh viên", example = "STU001")
    private String studentCode;

    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;

    @Schema(description = "Điểm trung bình", example = "8.5")
    private Double averageScore;

    @Schema(description = "Ngày sinh", example = "2000-01-15")
    private LocalDate dateOfBirth;
}
