package com.example.newsfeedproject.common.security.jwt;

import com.example.newsfeedproject.auth.dto.LoginUserRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

/**
 * 로그인 필터
 * /login 요청이 들어오면 요청을 가로채서 JWT를 발급해준다.
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

            // 임시 인증 토큰 생성 (아직 인증 x)
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
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 응답
    }
}
