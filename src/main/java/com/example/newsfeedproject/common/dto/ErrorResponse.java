package com.example.newsfeedproject.common.dto;

import com.example.newsfeedproject.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 공통 에러 응답 객체
 *
 * @author jiwon jung
 */
@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.status = errorCode.getStatus().value();
        this.code = errorCode.name();
        this.message = message;
    }
}