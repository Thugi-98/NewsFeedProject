package com.example.newsfeedproject.common.security.jwt;

import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.security.user.CustomUserDetailsService;
import com.example.newsfeedproject.common.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

// 클라이언트가 요청 시 보내는 JWT를 검증하여 사용자를 인증하고,
// 인증된 사용자의 정보를 SecurityContextHolder에 저장한다.
// 사용 시기: 로그인 이후 인증이 필요한 API 요청 시 작동
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    // 컨트롤러 밖으로 던져진 예외를 해결
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String jwtToken = null;

        String authorizationHeader = request.getHeader("Authorization");

        // 회원가입 or 로그인인지 확인(이 둘은 JWT 검사할 필요 x)
        if (requestURI.equals("/login") || requestURI.equals("/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 있는지 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("[{}] {}", ErrorCode.UNAUTHENTICATE_USER.name(), ErrorCode.UNAUTHENTICATE_USER.getMessage());

            CustomException customException = new CustomException(ErrorCode.UNAUTHENTICATE_USER);

            handlerExceptionResolver.resolveException(request, response, null, customException);
            return;
        }

        // 있으면 가져옴("Bearer " 떼고)
        jwtToken = authorizationHeader.substring(7);

        try {
            jwtUtil.validateToken(jwtToken);
        } catch (CustomException e) {
            log.error("[{}] {}", ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage());

            CustomException customException = new CustomException(ErrorCode.INVALID_TOKEN);

            handlerExceptionResolver.resolveException(request, response, null, customException);
            return;
        }

        // 최종적으로 token 검증 완료 -> 일시적인 session 생성
        String email = jwtUtil.extractEmail(jwtToken);

        UserDetails userDetails;

        try {
            // 인증 완료된 유저 정보 가져오기
            userDetails = userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            log.error("[{}] {}", ErrorCode.NOT_FOUND_USER.name(), ErrorCode.NOT_FOUND_USER.getMessage());

            CustomException customException = new CustomException(ErrorCode.NOT_FOUND_USER);

            handlerExceptionResolver.resolveException(request, response, null, customException);
            return;
        }

        // credentials: 로그인 후 비밀번호 들고 있을 이유 x -> 보안상 null
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, Collections.emptyList());

        // 인증된 사용자의 정보를 안전하게 저장 및 관리하기 위해 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        // 요청을 컨트롤러로 전달
        filterChain.doFilter(request, response);
    }
}
