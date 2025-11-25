package com.example.newsfeedproject.common.exception;

import com.example.newsfeedproject.common.dto.ErrorCode;
import lombok.Getter;

/**
 * 존재하지 않는 유저에 접근 시 발생하는 예외
 *
 * @author jiwon jung
 */
@Getter
public class UserNotFoundException extends CustomException {

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
