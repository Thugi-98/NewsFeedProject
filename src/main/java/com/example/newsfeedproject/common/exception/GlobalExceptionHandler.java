package com.example.newsfeedproject.common.exception;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 커스텀 예외 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(new ErrorResponse(
                        e.getErrorCode().getStatus().value(), e.getErrorCode().name(), e.getMessage())));
    }

    /**
     * 검증 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException e) {

        String defaultMessage = null;
        String field = null;

        if (e.getBindingResult().getFieldError() != null) {
            defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
            field = e.getBindingResult().getFieldError().getField();
        }

        ErrorResponse errorResponse = new ErrorResponse(
                e.getStatusCode().value(),                // HTTP status code
                field != null ? field : "VALIDATION_ERROR", // 어떤 필드에서 오류 났는지 표시
                defaultMessage != null ? defaultMessage : "Invalid request" // 실제 메시지
        );

        return ResponseEntity
                .status(e.getStatusCode())
                .body(ApiResponse.error(errorResponse));
    }


    /**
     * 나머지 예외 서버에러 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("INTERNAL_SERVER_ERROR")
                .message("서버 문제로 인해 실패하였습니다.")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(errorResponse));
    }
}