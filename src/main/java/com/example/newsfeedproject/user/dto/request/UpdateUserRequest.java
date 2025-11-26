package com.example.newsfeedproject.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "사용자 이름은 반드시 입력해야 합니다.")
    @Size(min = 1, max = 30, message = "사용자 이름은 1자 이상 30자 이하여야 합니다.")
    private String name;

    // 기존 비밀번호
    @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
    private String currentPassword;
    // 새로운 비밀번호
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    @Size(max = 100, message = "소개글은 100자 이하여야 합니다.")
    private String introduction;
}
