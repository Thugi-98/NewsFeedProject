package com.example.newsfeedproject.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 토큰 응답 DTO
@Getter
@RequiredArgsConstructor
public class TokenResponse {

    private final String token;
}
