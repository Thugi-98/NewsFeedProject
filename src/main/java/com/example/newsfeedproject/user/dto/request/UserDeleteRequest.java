package com.example.newsfeedproject.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserDeleteRequest {

    @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
    private String password;
}
