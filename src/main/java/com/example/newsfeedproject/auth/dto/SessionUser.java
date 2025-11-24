package com.example.newsfeedproject.auth.dto;

import com.example.newsfeedproject.common.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 세션에 저장될 유저 정보를 담는 DTO
 *
 * @author jiwon jung
 */
@Getter
@RequiredArgsConstructor
public class SessionUser {

    private final Long id;
    private final String name;
    private final String email;

    // 정적 팩토리 메소드
    public static SessionUser from(User user) {

        return new SessionUser(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
