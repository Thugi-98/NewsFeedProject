package com.example.newsfeedproject.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    private String name;

    // 기존 비밀번호
    private String currentPassword;
    // 새로운 비밀번호
    private String password;

    private LocalDate birth;
    private String introduction;
}
