package com.example.javatrainbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho response đăng nhập
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response đăng nhập")
public class LoginResponseDto {

    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Loại token", example = "Bearer")
    private String tokenType;

    @Schema(description = "Thời gian đăng nhập", example = "2025-01-10 10:30:00")
    private String loginAt;

    @Schema(description = "Thời gian hết hạn", example = "2025-01-10 11:30:00")
    private String expiresAt;

    @Schema(description = "ID người dùng", example = "1")
    private Integer userId;

    @Schema(description = "Tên đăng nhập", example = "admin")
    private String userName;
}
