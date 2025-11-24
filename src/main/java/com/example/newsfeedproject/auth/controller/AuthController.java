package com.example.newsfeedproject.auth.controller;

import com.example.newsfeedproject.auth.dto.*;
import com.example.newsfeedproject.auth.service.AuthService;
import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.exception.UnauthenticatedUserException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

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
     * 회원 가입 엔드포인트를 담당합니다.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<CreateUserResponse>> signup(@Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.signup(request)));
    }

    /**
     * 로그인 엔드포인트를 담당합니다.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginUserRequest request, HttpSession session) {

        SessionUser sessionUser = authService.login(request);
        session.setAttribute("loginUser", sessionUser); // 세션에 사용자 정보 저장

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * 로그아웃을 담당합니다.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpSession session,
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser) {

        // 세션에 저장된 유저가 아니면 예외
        if (sessionUser == null) {
            log.error("로그인 되지 않은 유저 {}", ErrorCode.UNAUTHENTICATE_USER);
            throw new UnauthenticatedUserException(ErrorCode.UNAUTHENTICATE_USER);
        }

        // 로그아웃 했으므로 세션 무력화
        session.invalidate();

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
