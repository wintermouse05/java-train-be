package com.example.javatrainbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho request đăng nhập
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request đăng nhập")
public class LoginRequestDto {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Schema(description = "Tên đăng nhập", example = "admin")
    private String userName;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Schema(description = "Mật khẩu", example = "123456")
    private String password;
}
