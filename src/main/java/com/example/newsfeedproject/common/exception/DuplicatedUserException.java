package com.example.newsfeedproject.common.exception;

import com.example.newsfeedproject.common.dto.ErrorCode;
import lombok.Getter;

/**
 * 이미 존재하는 사용자일 경우 발생하는 예외
 *
 * @author jiwon jung
 */
@Getter
public class DuplicatedUserException extends CustomException {

    public DuplicatedUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
