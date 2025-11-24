package com.example.newsfeedproject.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 공통 응답 객체
 *
 * @author jiwon jung
 */
@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    /**
     * 성공 시 응답
     *
     * @param data 성공 시 응답할 데이터 객체
     * @return ApiResponse
     * @param <T> 응답 DTO
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    /**
     * 에러 시 응답
     *
     * @param errorResponse 실패 시 응답할 에러 객체
     * @return ApiResponse
     */
    public static ApiResponse<?> error(ErrorResponse errorResponse) {
        return new ApiResponse<>(false, null, errorResponse);
    }
}