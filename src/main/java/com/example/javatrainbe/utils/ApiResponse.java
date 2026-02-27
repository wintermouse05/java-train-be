package com.example.javatrainbe.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO cho response API chung
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response API chung")
public class ApiResponse<T> {

    @Schema(description = "Mã trạng thái", example = "200")
    private Integer status;

    @Schema(description = "Thông báo", example = "Thành công")
    private String message;

    @Schema(description = "Dữ liệu trả về")
    private T data;

    @Schema(description = "Thời gian phản hồi", example = "2025-01-01T00:00:00")
    private String timestamp;

    /**
     * Response thành công
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Thành công");
        response.setData(data);
        response.setTimestamp(LocalDateTime.now().toString());
        return response;
    }

    /**
     * Response thành công với message tùy chỉnh
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now().toString());
        return response;
    }

    /**
     * Response lỗi
     */
    public static <T> ApiResponse<T> error(int status, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(null);
        response.setTimestamp(LocalDateTime.now().toString());
        return response;
    }

    /**
     * Response lỗi kèm data (dùng cho validation errors)
     */
    public static <T> ApiResponse<T> error(int status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now().toString());
        return response;
    }
}
