package com.example.newsfeedproject.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// 로그인 요청 DTO
@Getter
public class AuthLoginRequest {

    @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
    public String email;

    @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
    public String password;
}
