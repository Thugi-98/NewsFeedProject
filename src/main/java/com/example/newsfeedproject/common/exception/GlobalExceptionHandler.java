package com.example.newsfeedproject.common.exception;

import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //커스텀 예외처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getStatus().name())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    // @Valid 실패 처리 (Validation 예외)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        String message = "유효성 검사에 실패하였습니다.";
        if (ex.getBindingResult().getFieldError() != null) {
        }
        message = ex.getBindingResult().getFieldError().getDefaultMessage();

        ErrorResponse response = ErrorResponse.builder()
                .status(400)
                .code("BAD_REQUEST")
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(response);

    }

    // 나머지 예외 서버에러 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse response = ErrorResponse.builder()
                .status(500)
                .code("INTERNAL_SERVER_ERROR")
                .message("서버 문제로 인해 실패하였습니다.")
                .build();

        return ResponseEntity.internalServerError().body(response);
    }

}