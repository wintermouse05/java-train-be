package com.example.javatrainbe.service;

import com.example.javatrainbe.dao.UserDao;
import com.example.javatrainbe.dto.LoginRequestDto;
import com.example.javatrainbe.dto.LoginResponseDto;
import com.example.javatrainbe.dto.RegisterRequestDto;
import com.example.javatrainbe.dto.RegisterResponseDto;
import com.example.javatrainbe.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seasar.doma.jdbc.Result;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("Đăng nhập thành công")
        void shouldLoginSuccessfully() {
            User user = new User(1, "admin", "$2a$10$hashedPassword");
            LoginRequestDto request = new LoginRequestDto("admin", "123456");

            when(userDao.findByUserName("admin")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("123456", "$2a$10$hashedPassword")).thenReturn(true);
            when(jwtService.generateToken(1, "admin")).thenReturn("jwt-token-string");
            when(jwtService.getExpiration()).thenReturn(3600000L);

            LoginResponseDto result = authService.login(request);

            assertNotNull(result);
            assertEquals("jwt-token-string", result.getToken());
            assertEquals("Bearer", result.getTokenType());
            assertEquals(1, result.getUserId());
            assertEquals("admin", result.getUserName());
            assertNotNull(result.getLoginAt());
            assertNotNull(result.getExpiresAt());
        }

        @Test
        @DisplayName("Ném exception khi user không tồn tại")
        void shouldThrowExceptionWhenUserNotFound() {
            LoginRequestDto request = new LoginRequestDto("unknown", "123456");
            when(userDao.findByUserName("unknown")).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> authService.login(request));
            assertEquals("Sai tài khoản hoặc mật khẩu", exception.getMessage());
        }

        @Test
        @DisplayName("Ném exception khi mật khẩu sai")
        void shouldThrowExceptionWhenPasswordWrong() {
            User user = new User(1, "admin", "$2a$10$hashedPassword");
            LoginRequestDto request = new LoginRequestDto("admin", "wrongpass");

            when(userDao.findByUserName("admin")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrongpass", "$2a$10$hashedPassword")).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> authService.login(request));
            assertEquals("Sai tài khoản hoặc mật khẩu", exception.getMessage());
            verify(jwtService, never()).generateToken(any(), anyString());
        }
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("Đăng ký thành công")
        void shouldRegisterSuccessfully() {
            RegisterRequestDto request = new RegisterRequestDto("newuser", "123456", "123456");
            User savedUser = new User(1, "newuser", "$2a$10$encoded");

            when(userDao.findByUserName("newuser")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("123456")).thenReturn("$2a$10$encoded");

            @SuppressWarnings("unchecked")
            Result<User> userResult = mock(Result.class);
            when(userResult.getEntity()).thenReturn(savedUser);
            when(userDao.insert(any(User.class))).thenReturn(userResult);

            RegisterResponseDto result = authService.register(request);

            assertEquals(1, result.getUserId());
            assertEquals("newuser", result.getUserName());
            verify(userDao).insert(any(User.class));
        }

        @Test
        @DisplayName("Ném exception khi tài khoản đã tồn tại")
        void shouldThrowExceptionWhenUsernameExists() {
            RegisterRequestDto request = new RegisterRequestDto("admin", "123456", "123456");
            User existingUser = new User(1, "admin", "$2a$10$hash");

            when(userDao.findByUserName("admin")).thenReturn(Optional.of(existingUser));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> authService.register(request));
            assertEquals("Tài khoản đã tồn tại", exception.getMessage());
            verify(userDao, never()).insert(any());
        }

        @Test
        @DisplayName("Ném exception khi mật khẩu xác nhận không khớp")
        void shouldThrowExceptionWhenPasswordMismatch() {
            RegisterRequestDto request = new RegisterRequestDto("newuser", "123456", "654321");

            when(userDao.findByUserName("newuser")).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> authService.register(request));
            assertEquals("Mật khẩu xác nhận không khớp", exception.getMessage());
            verify(userDao, never()).insert(any());
        }
    }

    @Nested
    @DisplayName("validateToken")
    class ValidateToken {

        @Test
        @DisplayName("Trả về true khi token hợp lệ")
        void shouldReturnTrueForValidToken() {
            when(jwtService.validateToken("valid-token", "admin")).thenReturn(true);

            assertTrue(authService.validateToken("valid-token", "admin"));
        }

        @Test
        @DisplayName("Trả về false khi token không hợp lệ")
        void shouldReturnFalseForInvalidToken() {
            when(jwtService.validateToken("invalid-token", "admin")).thenReturn(false);

            assertFalse(authService.validateToken("invalid-token", "admin"));
        }
    }

    @Nested
    @DisplayName("getUsernameFromToken")
    class GetUsernameFromToken {

        @Test
        @DisplayName("Lấy username từ token thành công")
        void shouldExtractUsername() {
            when(jwtService.extractUsername("some-token")).thenReturn("admin");

            assertEquals("admin", authService.getUsernameFromToken("some-token"));
        }
    }
}
