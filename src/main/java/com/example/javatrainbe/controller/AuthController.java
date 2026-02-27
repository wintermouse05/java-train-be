package com.example.javatrainbe.controller;

import com.example.javatrainbe.dto.LoginRequestDto;
import com.example.javatrainbe.dto.LoginResponseDto;
import com.example.javatrainbe.dto.RegisterRequestDto;
import com.example.javatrainbe.dto.RegisterResponseDto;
import com.example.javatrainbe.service.AuthService;
import com.example.javatrainbe.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý Authentication (Login/Register)
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API đăng nhập và đăng ký")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * API đăng nhập
     */
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Đăng nhập với username và password, trả về JWT token")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }

    /**
     * API đăng ký
     */
    @PostMapping("/register")
    @Operation(summary = "Đăng ký", description = "Đăng ký tài khoản mới với username và password")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto registerRequest) {
        RegisterResponseDto response = authService.register(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công", response));
    }
}
