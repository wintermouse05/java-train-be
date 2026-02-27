package com.example.javatrainbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho response đăng ký
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response đăng ký tài khoản")
public class RegisterResponseDto {

    @Schema(description = "ID người dùng", example = "1")
    private Integer userId;

    @Schema(description = "Tên đăng nhập", example = "user1")
    private String userName;
}
