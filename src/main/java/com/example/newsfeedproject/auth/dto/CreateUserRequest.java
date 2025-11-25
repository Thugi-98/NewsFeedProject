package com.example.newsfeedproject.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 회원가입 요청 DTO
 *
 * @author jiwon jung
 */
@Getter
public class CreateUserRequest {

    @NotBlank(message = "사용자 이름은 반드시 입력해야 합니다.")
    @Size(min = 1, max = 30, message = "사용자 이름은 1자 이상 30자 이하여야 합니다.")
    private String name;

    @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    public String email;

    @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8자 이상 16자 이하 영문 대소문자, 숫자, 특수문자를 사용해야 합니다.")
    public String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Size(max = 100, message = "소개글은 100자 이하여야 합니다.")
    private String introduction;

}
