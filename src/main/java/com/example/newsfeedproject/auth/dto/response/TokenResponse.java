package com.example.newsfeedproject.auth.dto.response;

import com.example.newsfeedproject.common.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 토큰 응답 DTO
 *
 * @author jiwon jung
 */
@Getter
@RequiredArgsConstructor
public class TokenResponse {

    private final String token;
}
