package com.example.newsfeedproject.common.security.jwt;

import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.security.utils.JwtUtil;
import com.example.newsfeedproject.user.dto.request.LoginUserRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 로그인 요청을 처리하고, 인증 성공 시 JWT 발급 메소드 호출한다.
 * /login 요청이 들어오면 요청을 가로채서 JWT를 발급해준다.
 * UsernamePasswordAuthenticationFilter는 스프링 시큐리티에서 폼 로그인을 담당하는 기본 필터이다.
 * /login 요청이 오면 필터 체인이 이 필터까지 내려와서 검사 -> username, password 읽고 AuthenticationManager에게 인증을 위임한다.
 * 인증 성공 및 실패에 따라 메소드 자동 실행한다.
 * 이 UsernamePasswordAuthenticationFilter를 상속해서 JWT 발급용으로 바꾼 클래스이다.
 *
 * @author jiwon jung
 */
@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; // 인증 관리자 (로그인 성공 시킬지 말지 결정)
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * 로그인 요청 시 사용자 인증 처리 시도
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = null;

        try {
            // JSON 데이터를 자바 객체로 파싱
            LoginUserRequest loginUserRequest = objectMapper.readValue(request.getInputStream(), LoginUserRequest.class);

            String email = loginUserRequest.getEmail();
            String password = loginUserRequest.getPassword();

            // 인증 정보를 담는 토큰 객체를 만듦 (여기서는 아직 인증 x)
            // 사용자가 입력한 이메일, 비밀번호를 담아 AuthenticationManger로 전달하는 임시 용도
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

            // 실제 검증 수행
            // 이 메소드 호출 시, 내부적으로 UserDetailsService의 loadUserByUsername() 호출(비밀번호 일치까지 다 해줌)
            authentication = authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            log.error("[{}] {}", ErrorCode.NOT_FOUND_USER.name(), ErrorCode.LOGIN_FAIL.getMessage());

            CustomException customException = new CustomException(ErrorCode.LOGIN_FAIL);

            handlerExceptionResolver.resolveException(request, response, null, customException);

        } catch (Exception e) {
            log.error("[{}] {}", HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SEVER_ERROR.getMessage());

            CustomException customException = new CustomException(ErrorCode.INTERNAL_SEVER_ERROR);

            handlerExceptionResolver.resolveException(request, response, null, customException);
        }

        return authentication;
    }

    /**
     * 로그인 성공 시
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        // 인증된 사용자 정보에서 이메일 꺼내기
        String email = authentication.getName();
        
        // Access Token 생성
        String accessToken = jwtUtil.createAccessToken(email);

        // Refresh Token 생성
        String refreshToken = jwtUtil.createRefreshToken(email);

        // Authorization 헤더로 Access Token 전달
        response.setHeader("Authorization", accessToken);

        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtUtil.substringToken(refreshToken)); // 쿠키에는 "Bearer " 빼고 저장

        refreshTokenCookie.setHttpOnly(true); // JS에서 접근 불가
        refreshTokenCookie.setSecure(true); // HTTPS 환경에서만 전송 가능
        refreshTokenCookie.setPath("/"); // 전체 경로에서 사용 가능
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일 (쿠키 만료는 브라우저 기준)

        response.addCookie(refreshTokenCookie); // 쿠키에 추가

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * 로그인 실패 시
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        handlerExceptionResolver.resolveException(request, response, null, e);
    }
}
