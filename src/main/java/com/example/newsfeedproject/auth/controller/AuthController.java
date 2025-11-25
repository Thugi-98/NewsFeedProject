package com.example.newsfeedproject.auth.controller;

import com.example.newsfeedproject.auth.dto.*;
import com.example.newsfeedproject.auth.service.AuthService;
import com.example.newsfeedproject.common.dto.ApiResponse;
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
     * 회원 가입 엔드포인트를 담당합니다.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<CreateUserResponse>> signup(@Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.signup(request)));
    }

}
