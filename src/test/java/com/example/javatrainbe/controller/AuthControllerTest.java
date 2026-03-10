package com.example.javatrainbe.controller;

import com.example.javatrainbe.dto.LoginRequestDto;
import com.example.javatrainbe.dto.LoginResponseDto;
import com.example.javatrainbe.dto.RegisterRequestDto;
import com.example.javatrainbe.dto.RegisterResponseDto;
import com.example.javatrainbe.exception.GlobalExceptionHandler;
import com.example.javatrainbe.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /api/auth/login")
    class Login {

        @Test
        @DisplayName("Trả về 200 và token khi đăng nhập thành công")
        void shouldLoginSuccessfully() throws Exception {
            LoginRequestDto request = new LoginRequestDto("admin", "123456");
            LoginResponseDto response = new LoginResponseDto(
                    "jwt-token", "Bearer",
                    "2026-03-10 10:00:00", "2026-03-10 11:00:00",
                    1, "admin");

            when(authService.login(any(LoginRequestDto.class))).thenReturn(response);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Đăng nhập thành công"))
                    .andExpect(jsonPath("$.data.token").value("jwt-token"))
                    .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                    .andExpect(jsonPath("$.data.userId").value(1))
                    .andExpect(jsonPath("$.data.userName").value("admin"));

            verify(authService).login(any(LoginRequestDto.class));
        }

        @Test
        @DisplayName("Trả về 500 khi đăng nhập thất bại")
        void shouldReturn500WhenLoginFails() throws Exception {
            LoginRequestDto request = new LoginRequestDto("admin", "wrongpass");

            when(authService.login(any(LoginRequestDto.class)))
                    .thenThrow(new RuntimeException("Sai tài khoản hoặc mật khẩu"));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("Sai tài khoản hoặc mật khẩu"));
        }
    }

    @Nested
    @DisplayName("POST /api/auth/register")
    class Register {

        @Test
        @DisplayName("Trả về 200 khi đăng ký thành công")
        void shouldRegisterSuccessfully() throws Exception {
            RegisterRequestDto request = new RegisterRequestDto("newuser", "123456", "123456");
            RegisterResponseDto response = new RegisterResponseDto(1, "newuser");

            when(authService.register(any(RegisterRequestDto.class))).thenReturn(response);

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Đăng ký thành công"))
                    .andExpect(jsonPath("$.data.userId").value(1))
                    .andExpect(jsonPath("$.data.userName").value("newuser"));

            verify(authService).register(any(RegisterRequestDto.class));
        }

        @Test
        @DisplayName("Trả về 500 khi đăng ký thất bại")
        void shouldReturn500WhenRegisterFails() throws Exception {
            RegisterRequestDto request = new RegisterRequestDto("admin", "123456", "123456");

            when(authService.register(any(RegisterRequestDto.class)))
                    .thenThrow(new RuntimeException("Tài khoản đã tồn tại"));

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("Tài khoản đã tồn tại"));
        }
    }
}
