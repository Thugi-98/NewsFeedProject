package com.example.newsfeedproject.common.exception;

import com.example.newsfeedproject.common.dto.ErrorCode;
import lombok.Getter;

/**
 * 로그인 되지 않은 사용자일 때 발생하는 예외
 *
 * @author jiwon jung
 */
@Getter
public class UnauthenticatedUserException extends CustomException {

    public UnauthenticatedUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
