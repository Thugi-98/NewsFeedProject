package com.example.newsfeedproject.common.security.jwt;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.dto.ErrorResponse;
import com.example.newsfeedproject.common.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * CustomAccessDeniedHandler는 AccessDeniedException만 처리하고
 * CustomAuthenticationEntryPoint는 AuthenticationException 처리한다.
 * 다른 커스텀 예외를 처리해주기 위한 별도의 필터 클래스이다.
 */
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    e.getErrorCode(),
                    e.getMessage());

            // ApiResponse 객체 생성
            ApiResponse<?> apiResponse = ApiResponse.error(errorResponse);

            // JSON으로 직렬화
            String json = objectMapper.writeValueAsString(apiResponse);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(errorResponse.getStatus());
            response.getWriter().write(json);
        }
    }
}
