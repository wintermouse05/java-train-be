package com.example.javatrainbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO cho request tạo/cập nhật student
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request tạo/cập nhật student")
public class StudentRequestDto {

    @NotBlank(message = "Tên sinh viên không được để trống")
    @Size(max = 20, message = "Tên sinh viên không được quá 20 ký tự")
    @Schema(description = "Tên sinh viên", example = "Nguyễn Văn A")
    private String studentName;

    @NotBlank(message = "Mã sinh viên không được để trống")
    @Size(max = 10, message = "Mã sinh viên không được quá 10 ký tự")
    @Schema(description = "Mã sinh viên", example = "STU001")
    private String studentCode;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được quá 255 ký tự")
    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;

    @Schema(description = "Điểm trung bình", example = "8.5")
    private Double averageScore;

    @Schema(description = "Ngày sinh", example = "2000-01-15")
    private LocalDate dateOfBirth;
}
