package com.example.newsfeedproject.common.exception;

import com.example.newsfeedproject.common.dto.ErrorCode;
import lombok.Getter;

/**
 * 비밀번호 불일치 시 발생하는 예외
 *
 * @author jiwon jung
 */
@Getter
public class InvalidPasswordException extends CustomException {

    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
