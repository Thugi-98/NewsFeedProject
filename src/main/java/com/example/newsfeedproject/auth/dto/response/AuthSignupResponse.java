package com.example.newsfeedproject.auth.dto.response;

import com.example.newsfeedproject.common.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 회원가입 응답 DTO
@Getter
@RequiredArgsConstructor
public class AuthSignupResponse {

    private final Long id;
    private final String name;
    public final String email;
    private final LocalDate birth;
    private final String introduction;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static AuthSignupResponse from(User user) {

        return new AuthSignupResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBirth(),
                user.getIntroduction(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}
