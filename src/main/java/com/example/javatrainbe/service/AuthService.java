package com.example.javatrainbe.service;

import com.example.javatrainbe.dao.UserDao;
import com.example.javatrainbe.dto.LoginRequestDto;
import com.example.javatrainbe.dto.LoginResponseDto;
import com.example.javatrainbe.dto.RegisterRequestDto;
import com.example.javatrainbe.dto.RegisterResponseDto;
import com.example.javatrainbe.entity.User;
import com.example.javatrainbe.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service xử lý authentication (Login/Register)
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Xử lý đăng nhập
     */
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userDao.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("Sai tài khoản hoặc mật khẩu"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai tài khoản hoặc mật khẩu");
        }

        // Tạo JWT token qua JwtService
        String token = jwtService.generateToken(user.getUserId(), user.getUserName());

        // Format thời gian
        long now = System.currentTimeMillis();
        String loginAt = DateTimeUtils.formatEpochMillis(now);
        String expiresAt = DateTimeUtils.formatEpochMillis(now + jwtService.getExpiration());

        return new LoginResponseDto(token, "Bearer", loginAt, expiresAt,
                user.getUserId(), user.getUserName());
    }

    /**
     * Xử lý đăng ký
     */
    public RegisterResponseDto register(RegisterRequestDto request) {
        // Kiểm tra tài khoản đã tồn tại
        if (userDao.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Tài khoản đã tồn tại");
        }

        // Kiểm tra mật khẩu xác nhận
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        // Tạo user mới (immutable entity)
        User user = new User(request.getUserName(), passwordEncoder.encode(request.getPassword()));
        userDao.insert(user);

        // Trả về response
        return new RegisterResponseDto(user.getUserId(), user.getUserName());
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token, String username) {
        return jwtService.validateToken(token, username);
    }

    /**
     * Lấy username từ token
     */
    public String getUsernameFromToken(String token) {
        return jwtService.extractUsername(token);
    }
}
