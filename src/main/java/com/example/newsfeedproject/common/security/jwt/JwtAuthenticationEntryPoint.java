package com.example.newsfeedproject.common.security.jwt;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // Http Status 설정 (401 Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Content-Type 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorBody = new ErrorResponse(
                ErrorCode.UNAUTHENTICATE_USER,
                ErrorCode.UNAUTHENTICATE_USER.getMessage()
        );

        ApiResponse<?> errorResponse = ApiResponse.error(errorBody);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
