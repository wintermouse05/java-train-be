package com.example.javatrainbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho request đăng ký
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request đăng ký tài khoản")
public class RegisterRequestDto {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 20, message = "Tên đăng nhập không được quá 20 ký tự")
    @Schema(description = "Tên đăng nhập", example = "admin")
    private String userName;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 15, message = "Mật khẩu phải từ 6 đến 15 ký tự")
    @Schema(description = "Mật khẩu", example = "123456")
    private String password;

    @NotBlank(message = "Mật khẩu xác nhận không được để trống")
    @Schema(description = "Mật khẩu xác nhận", example = "123456")
    private String confirmPassword;
}
