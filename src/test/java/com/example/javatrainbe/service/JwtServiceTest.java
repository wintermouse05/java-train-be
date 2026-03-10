package com.example.javatrainbe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    // Base64 encoded secret key (at least 32 bytes for HS256)
    private static final String TEST_SECRET = "VGhpc0lzQVZlcnlTZWN1cmVTZWNyZXRLZXlGb3JKV1RUb2tlblNpZ25pbmdIUzI1Ng==";
    private static final long TEST_EXPIRATION = 3600000L; // 1 hour

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        // Inject private fields via reflection (since @Value won't work in unit test)
        Field secretField = JwtService.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtService, TEST_SECRET);

        Field expirationField = JwtService.class.getDeclaredField("jwtExpiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, TEST_EXPIRATION);
    }

    @Nested
    @DisplayName("generateToken")
    class GenerateToken {

        @Test
        @DisplayName("Tạo token thành công")
        void shouldGenerateToken() {
            String token = jwtService.generateToken(1, "admin");

            assertNotNull(token);
            assertFalse(token.isEmpty());
            // JWT has 3 parts separated by '.'
            assertEquals(3, token.split("\\.").length);
        }
    }

    @Nested
    @DisplayName("extractUsername")
    class ExtractUsername {

        @Test
        @DisplayName("Trích xuất username đúng")
        void shouldExtractUsername() {
            String token = jwtService.generateToken(1, "admin");

            String username = jwtService.extractUsername(token);

            assertEquals("admin", username);
        }
    }

    @Nested
    @DisplayName("extractUserId")
    class ExtractUserId {

        @Test
        @DisplayName("Trích xuất userId đúng")
        void shouldExtractUserId() {
            String token = jwtService.generateToken(42, "testuser");

            Integer userId = jwtService.extractUserId(token);

            assertEquals(42, userId);
        }
    }

    @Nested
    @DisplayName("extractExpiration")
    class ExtractExpiration {

        @Test
        @DisplayName("Trích xuất thời gian hết hạn")
        void shouldExtractExpiration() {
            String token = jwtService.generateToken(1, "admin");

            Date expiration = jwtService.extractExpiration(token);

            assertNotNull(expiration);
            assertTrue(expiration.after(new Date()));
        }
    }

    @Nested
    @DisplayName("isTokenExpired")
    class IsTokenExpired {

        @Test
        @DisplayName("Token chưa hết hạn")
        void shouldReturnFalseForValidToken() {
            String token = jwtService.generateToken(1, "admin");

            assertFalse(jwtService.isTokenExpired(token));
        }

        @Test
        @DisplayName("Token đã hết hạn")
        void shouldReturnTrueForExpiredToken() throws Exception {
            // Set expiration to 0ms to create an expired token immediately
            Field expirationField = JwtService.class.getDeclaredField("jwtExpiration");
            expirationField.setAccessible(true);
            expirationField.set(jwtService, 0L);

            String token = jwtService.generateToken(1, "admin");
            // Small delay to ensure the token expires
            Thread.sleep(10);

            assertTrue(jwtService.isTokenExpired(token));
        }
    }

    @Nested
    @DisplayName("validateToken")
    class ValidateToken {

        @Test
        @DisplayName("Token hợp lệ với đúng username")
        void shouldReturnTrueForValidToken() {
            String token = jwtService.generateToken(1, "admin");

            assertTrue(jwtService.validateToken(token, "admin"));
        }

        @Test
        @DisplayName("Token không hợp lệ với sai username")
        void shouldReturnFalseForWrongUsername() {
            String token = jwtService.generateToken(1, "admin");

            assertFalse(jwtService.validateToken(token, "wronguser"));
        }

        @Test
        @DisplayName("Token không hợp lệ khi bị thay đổi")
        void shouldReturnFalseForTamperedToken() {
            String token = jwtService.generateToken(1, "admin");
            String tamperedToken = token + "tampered";

            assertFalse(jwtService.validateToken(tamperedToken, "admin"));
        }

        @Test
        @DisplayName("Token hết hạn trả về false")
        void shouldReturnFalseForExpiredToken() throws Exception {
            Field expirationField = JwtService.class.getDeclaredField("jwtExpiration");
            expirationField.setAccessible(true);
            expirationField.set(jwtService, 0L);

            String token = jwtService.generateToken(1, "admin");
            Thread.sleep(10);

            assertFalse(jwtService.validateToken(token, "admin"));
        }
    }

    @Nested
    @DisplayName("getExpiration")
    class GetExpiration {

        @Test
        @DisplayName("Trả về giá trị expiration đúng")
        void shouldReturnExpiration() {
            assertEquals(TEST_EXPIRATION, jwtService.getExpiration());
        }
    }
}
