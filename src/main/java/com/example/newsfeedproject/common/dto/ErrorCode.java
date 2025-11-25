package com.example.newsfeedproject.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "비밀번호가 틀립니다."),

    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다."),
    PASSWORD_IS_SAME(HttpStatus.UNAUTHORIZED, "현재 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다."),

    UNAUTHENTICATE_USER(HttpStatus.UNAUTHORIZED, "로그인 되지 않은 사용자입니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}