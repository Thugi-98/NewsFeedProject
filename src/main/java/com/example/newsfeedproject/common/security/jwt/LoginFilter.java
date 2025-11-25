package com.example.newsfeedproject.common.security.jwt;

import com.example.newsfeedproject.user.dto.request.LoginUserRequest;
import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.dto.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 로그인 요청을 처리하고, 인증 성공 시 JWT 발급 메소드 호출
 * /login 요청이 들어오면 요청을 가로채서 JWT를 발급해준다.
 * ✅ 사용 시기: /login 엔드포인트
 *
 * @author jiwon jung
 */
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; // 인증 관리자(로그인 성공 시킬지 말지 결정)
    private final JWTUtil jwtUtil;

    /**
     * 로그인 요청 시 사용자 인증 처리
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            // JSON 데이터를 자바 객체로 파싱
            ObjectMapper mapper = new ObjectMapper();
            LoginUserRequest loginUserRequest = mapper.readValue(request.getInputStream(), LoginUserRequest.class);

            String email = loginUserRequest.getEmail();
            String password = loginUserRequest.getPassword();

            // 인증 정보를 담는 토큰 객체를 만듦 (여기서는 아직 인증 x)
            // 사용자가 입력한 이메일, 비밀번호를 담아 AuthenticationManger로 전달하는 임시 용도
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

            // 실제 검증 수행
            // 이 메소드 호출 시, 내부적으로 UserDetailsService의 loadUserByUsername() 호출(비밀번호 일치까지 다 해줌)
            return authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 로그인 성공 시
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {

        // 인증된 사용자 정보에서 이메일 꺼내기
        String email = authResult.getName();
        
        // JWT 토큰 생성
        String token = jwtUtil.createJwt(email);

        // Authorization 헤더로 JWT 전달
        response.setHeader("Authorization", token);

        response.setStatus(HttpServletResponse.SC_NO_CONTENT); // Body 없이 204 No Content
    }

    /**
     * 로그인 실패 시
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        String message;
        String code;

        if (failed instanceof UsernameNotFoundException) {
            message = "가입되지 않은 이메일입니다.";
            code = "USER_NOT_FOUND";
        } else if (failed instanceof BadCredentialsException) {
            message = "비밀번호가 일치하지 않습니다.";
            code = "BAD_CREDENTIALS";
        } else {
            message = "로그인 실패";
            code = "LOGIN_FAILED";
        }

        // ErrorResponse 객체 생성
        ErrorResponse errorResponse = new ErrorResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                code,
                message
        );

        // ApiResponse 객체 생성
        ApiResponse<?> apiResponse = ApiResponse.error(errorResponse);
        
        // JSON으로 직렬화
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(apiResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 응답
        response.getWriter().write(json );
    }
}
