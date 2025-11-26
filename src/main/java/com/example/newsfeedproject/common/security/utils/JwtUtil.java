package com.example.newsfeedproject.common.security.utils;

import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.HS256;

/**
 * JWT 토큰의 생성, 검증, 유효성 검사, Claim 추출을 담당하는 유틸 클래스
 *
 * @author jiwon jung
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // "소유자" 라는 뜻을 가진 JWT 토큰 접두사이며, 토큰을 가진 사람(Bearer)가 인증된 사용자라는 뜻
    private final String BEARER_PREFIX = "Bearer ";

    // Access Token 만료 시간: 60분 (ms)
    // *(11.26 21:35) 테스트를 위해  1분으로 설정
    private final long ACCESS_TOKEN_TIME = 60 * 1 * 1000L;

    // Refresh Token 만료 시간: 일주일 (ms)
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 7 * 1000L;

    // application.yml에 있는 secret-key를 주입받은 비밀 키
    @Value("${spring.jwt.secret-key}")
    private String secretKey;
    
    // 실제 서명에 사용되는 키 객체
    private SecretKey key;

    // Jackson 라이브러리의 핵심 클래스이자 JSON과 자바 객체 간의 변환을 담당하는 핵심 컴포넌트 (번역가 역할)
    // 자바 객체 -> JSON (직렬화), JSON -> 자바 객체 (역직렬화)
    private final ObjectMapper objectMapper;

    /**
     * 의존성 주입이 이루어진 뒤에 초기화를 수행하는 메소드
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); // Base64 인코딩 된 문자열인 비밀 키 디코딩
        this.key = Keys.hmacShaKeyFor(bytes); // HMAC SHA-256 알고리즘 (대칭 키)
    }
    
    /**
     * payload(claim)에서 email을 추출하는 메소드
     */
    public String extractEmail(String token) {

        return Jwts.parser() // JWT 문자열을 해석(파싱) 하고 검증할 파서를 생성
                .verifyWith(key) // 파서에게 어떤 키로 검증할 지 알려줌
                .build() // 위에 설정값을 바탕으로 실제 동작하는 파서 인스턴스 생성
                .parseSignedClaims(token) // 전달한 token 문자열을 파싱하고 서명이 맞는지 검사 후 Claims를 포함한 JWT 객체 리턴
                .getPayload() // payload를 꺼냄
                .get("email", String.class); // 페이로드에서 키 값이 "email"에 해당하는 값을 꺼내서 String으로 변환해서 리턴
    }

    /**
     * JWT 토큰 만료 여부를 확인하는 메소드
     */
    public Boolean isTokenExpired(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration() // Claims 안에 들어있는 exp 값을 꺼냄 (JWT 표준 클레임, 토큰의 만료 시각을 의미)
                .before(new Date()); // 현재 시간(new Date())보다 이전이면 true -> 만료 되었음을 의미
    }

    /**
     * Access Token을 생성하는 메소드
     * email, 토큰 만료 시간(expiredMs)을 포함한 JWT 발급
     */
    public String createAccessToken(String email) {

        return BEARER_PREFIX +
                Jwts.builder()
                        .claim("email", email) // 이메일
                        .issuedAt(new Date(System.currentTimeMillis())) // 발급 시간 설정
                        .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME)) // 만료 시간 설정
                        .signWith(key, HS256) // 비밀 키와 알고리즘을 사용하여 서명
                        .compact(); // JWT 토큰을 문자열로 변환하여 리턴
    }

    /**
     * Refresh Token을 생성하는 메소드
     * 쿠키에 담길 거라서 BEARER_PREFIX 붙이지 않음 (붙일 시 에러)
     */
    public String createRefreshToken(String email) {

        return BEARER_PREFIX +
                Jwts.builder()
                        .claim("email", email)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                        .signWith(key, HS256)
                        .compact();
    }

    /**
     * Bearer를 떼고 순수 토큰만 가져오는 메소드
     */
    public String substringToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }

        log.error("[Not Found Token] 토큰을 찾을 수 없습니다.");
        throw new CustomException(ErrorCode.WRONG_TYPE_TOKEN);
    }

    /**
     * JWT 토큰의 유효성을 검증한다.
     */
    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("[Invalid JWT signature] 유효하지 않은 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("[Expired JWT token] 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("[Unsupported JWT token] 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("[JWT claims is empty] 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
    
//    private void jwtExceptionHandler(ErrorCode errorCode, HttpServletResponse response) throws IOException {
//        ErrorResponse errorResponse = new ErrorResponse(errorCode, errorCode.getMessage());
//
//        response.setStatus(errorResponse.getStatus());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
//    }
}
