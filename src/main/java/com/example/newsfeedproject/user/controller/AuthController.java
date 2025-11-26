package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.security.utils.JwtUtil;
import com.example.newsfeedproject.user.dto.request.CreateUserRequest;
import com.example.newsfeedproject.user.dto.response.CreateUserResponse;
import com.example.newsfeedproject.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 사용자 인증, 인가 엔드포인트를 담당하는 컨트롤러
 *
 * @author jiwon jung
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jWTUtil;

    /**
     * 회원 가입 엔드포인트를 담당한다.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<CreateUserResponse>> signupApi(@Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.signup(request)));
    }

    /**
     * 로그아웃 엔드포인트를 담당한다.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("refreshToken", null); // 쿠키 삭제
        cookie.setHttpOnly(true); // JS 상에서 토큰 접근 못 하도록 막음
        cookie.setSecure(true); // HTTPS에서만 접근이 가능하도록 함
        cookie.setPath("/"); // 쿠키가 유효한 URL 지정
        cookie.setMaxAge(0); // 만료 처리
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> reissue(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        // 토큰이 비었는지 확인
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.WRONG_TYPE_TOKEN);
        }

        // 토큰 유효성 검증
        if (!jWTUtil.validateToken(refreshToken) || jWTUtil.isTokenExpired(refreshToken)) {
            throw new CustomException(ErrorCode.WRONG_TYPE_TOKEN);
        }

        String email = jWTUtil.extractEmail(refreshToken);

        // AccessToken 재발급
        String newAccessToken = jWTUtil.createAccessToken(email);

        response.setHeader("Authorization", newAccessToken);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
