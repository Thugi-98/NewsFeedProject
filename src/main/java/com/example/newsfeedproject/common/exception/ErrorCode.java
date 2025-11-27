package com.example.newsfeedproject.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다."),
    PASSWORD_IS_SAME(HttpStatus.UNAUTHORIZED, "현재 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다."),

    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "이메일이나 비밀번호가 일치하지 않습니다."),

    UNAUTHENTICATE_USER(HttpStatus.UNAUTHORIZED, "로그인 되지 않은 사용자입니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),

    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 비어 있습니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 타입의 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    CANT_FOLLOW_MYSELF(HttpStatus.FORBIDDEN, "내 계정은 팔로우할 수 없습니다."),
    ALREADY_FOLLOW(HttpStatus.CONFLICT, "해당 유저는 이미 팔로우 중입니다."),

    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 상의 에러입니다."),
    CANNOT_FOLLOW_SELF(HttpStatus.FORBIDDEN, "내 계정은 팔로우할 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.CONFLICT, "해당 유저는 이미 팔로우 중입니다."),

    FOLLOW_LIST_PRIVATE(HttpStatus.FORBIDDEN, "비공개 팔로잉 목록입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우중인 계정이 아닙니다."),

    ALREADY_POSTLIKE(HttpStatus.CONFLICT, "이미 좋아요를 표시한 게시글입니다."),
    POSTLIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 표시한 게시물이 아닙니다."),

    ALREADY_COMMENTLIKE(HttpStatus.CONFLICT, "이미 좋아요를 표시한 댓글입니다."),
    COMMENTLIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 표시한 댓글이 아닙니다.")
    ;

    private final HttpStatus status;
    private final String message;
}