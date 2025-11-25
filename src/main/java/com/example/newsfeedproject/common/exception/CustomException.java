package com.example.newsfeedproject.common.exception;

import com.example.newsfeedproject.common.dto.ErrorCode;
import lombok.Getter;

// 커스텀 에러 클래스
@Getter
public class CustomException extends RuntimeException {

    //속성
    private final ErrorCode errorCode;

    //생성자
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}