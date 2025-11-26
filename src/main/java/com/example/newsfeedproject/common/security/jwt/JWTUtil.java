package com.example.newsfeedproject.common.security.jwt;

import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.dto.ErrorResponse;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.HS256;

/**
 * JWT 토큰의 생성, 검증, 유효성 검사 및 Claim 추출을 담당하는 유틸 클래스
 *
 * @author jiwon jung
 */
@Slf4j
@Component
public class JWTUtil {

    public static final String BEARER_PREFIX = "Bearer "; // JWT 토큰 접두사
    private final long ACCESS_TOKEN_TIME = 60 * 1 * 1000L; // 60분 (ms)
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 7 * 1000L; // 일주일 (ms)
    private final SecretKey secretKey; // ⚠️ 서명에 사용되는 비밀 키를 코드 내부에 하드코딩 하지 말 것, 설정 파일(application.yml)에서 관리 권장
    private final ObjectMapper objectMapper;

    /**
     * 설정 파일에 적어둔 비밀 키(secret)를 실제 HMAC-SHA256 서명에 사용할 수 있는 SecretKey 객체로 변환한다.
     * JWT 서명(Signature)을 생성하는데 필요한 Key 객체를 생성하는 과정이다.
     * 1. @Value("${spring.jwt.secret}") - application.yml에 있는 문자열 secret을 받아온다.
     * 2. secret.getBytes(StandardCharsets.UTF_8) - HMAC 알고리즘은 바이트 기반 key가 필요하기에 바이트 배열로 변환한다.
     * 3. SecretKeySpec - 비밀 키 생성 클래스. 바이트 배열을 JCA에서 사용하는 표준 SecretKey로 변환한다.
     * 4. Jwts.SIG.HS256.key().build().getAlgorithm() - HS256 알고리즘 이름을 가져온다.
     */
    public JWTUtil(@Value("${spring.jwt.secret}") String secret, ObjectMapper objectMapper) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HS256.key().build().getAlgorithm());
        this.objectMapper = objectMapper;
    }
    
    /**
     * JWT에서 email 추출
     * 토큰 검증 후 payload에서 email claim을 추출한다.
     */
    public String getEmail(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)                    // 파서를 어떤 키로 검증할 지 지정
                .build()
                .parseSignedClaims(token)                 // 전달한 token 문자열을 파싱하고 서명이 맞는지 검사 후 Claims 반환
                .getPayload()                             // payload를 꺼낸다
                .get("email", String.class);    // 인증된 사용자 식별자로 name 사용
    }

    /**
     * JWT 만료 여부 확인
     */
    public Boolean isTokenExpired(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    /**
     * Access Token을 생성하는 메소드
     * email, 토큰 만료 시간(expiredMs)을 포함한 JWT 발급
     */
    public String createAccessToken(String email) {

        return BEARER_PREFIX +
                Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))                 // 발급 시간 설정
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))   // 만료 시간 설정
                .signWith(secretKey) // 비밀키를 사용하여 서명
                .compact();
    }

    /**
     * Refresh Token을 생성하는 메소드
     */
    public String createRefreshToken(String email) {

        return Jwts.builder()
                        .claim("email", email)
                        .issuedAt(new Date(System.currentTimeMillis()))                 // 발급 시간 설정
                        .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))   // 만료 시간 설정
                        .signWith(secretKey) // 비밀키를 사용하여 서명
                        .compact();
    }

    /**
     * JWT 토큰의 유효성을 검증한다.
     */
    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            log.error("JWT ERROR");
        }

        return false;
    }

    /**
     * 예외 처리
     * @param errorCode
     * @param response
     * @throws IOException
     */
    private void jwtExceptionHandler(ErrorCode errorCode, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, errorCode.getMessage());

        response.setStatus(errorResponse.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
