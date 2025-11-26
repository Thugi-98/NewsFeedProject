package com.example.newsfeedproject.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    @Size(min = 1, max = 30, message = "사용자 이름은 1자 이상 30자 이하여야 합니다.")
    private String name;

    // 기존 비밀번호
    @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
    private String currentPassword;
    // 새로운 비밀번호
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8자 이상 16자 이하 영문 대소문자, 숫자, 특수문자를 사용해야 합니다.")
    private String newPassword;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    @Size(max = 100, message = "소개글은 100자 이하여야 합니다.")
    private String introduction;

    private Boolean followPrivate;
}
