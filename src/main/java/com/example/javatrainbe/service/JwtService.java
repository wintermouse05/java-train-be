package com.example.javatrainbe.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

/**
 * Service xử lý JWT token
 * Tách riêng logic JWT ra khỏi AuthService để tái sử dụng
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpiration;

    /**
     * Tạo JWT token chứa userId, username
     */
    public String generateToken(Integer userId, String username) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
            JWSSigner signer = new MACSigner(key);

            long now = System.currentTimeMillis();
            Date issuedAt = new Date(now);
            Date expiration = new Date(now + jwtExpiration);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("userId", userId)
                    .issueTime(issuedAt)
                    .expirationTime(expiration)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256), claims);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo JWT token", e);
        }
    }

    /**
     * Lấy username từ token
     */
    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Token không hợp lệ", e);
        }
    }

    /**
     * Lấy userId từ token
     */
    public Integer extractUserId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getIntegerClaim("userId");
        } catch (ParseException e) {
            throw new RuntimeException("Token không hợp lệ", e);
        }
    }

    /**
     * Lấy expiration date từ token
     */
    public Date extractExpiration(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new RuntimeException("Token không hợp lệ", e);
        }
    }

    /**
     * Kiểm tra token có hết hạn không
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token, String username) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
            MACVerifier verifier = new MACVerifier(key);

            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean signatureValid = signedJWT.verify(verifier);
            String extractedUsername = signedJWT.getJWTClaimsSet().getSubject();

            return signatureValid && extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy thời gian hết hạn (milliseconds)
     */
    public long getExpiration() {
        return jwtExpiration;
    }
}
