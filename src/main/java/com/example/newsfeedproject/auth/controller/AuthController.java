package com.example.newsfeedproject.auth.controller;

import com.example.newsfeedproject.auth.dto.request.LoginUserRequest;
import com.example.newsfeedproject.auth.dto.response.TokenResponse;
import com.example.newsfeedproject.auth.service.AuthService;
import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.auth.dto.request.SignupUserRequest;
import com.example.newsfeedproject.auth.dto.response.SignupUserResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 회원 가입 엔드포인트를 담당한다.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupUserResponse>> signupApi(@Valid @RequestBody SignupUserRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.signup(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginApi(@Valid @RequestBody LoginUserRequest request, HttpServletResponse response) {

        TokenResponse tokenResponse = authService.login(request);

        // Authorization 헤더로 JWT 토큰 전달
        response.setHeader("Authorization", tokenResponse.getToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 로그아웃 엔드포인트를 담당한다.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutApi() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
