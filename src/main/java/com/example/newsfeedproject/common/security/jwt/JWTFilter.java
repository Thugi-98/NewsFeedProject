package com.example.newsfeedproject.common.security.jwt;

import com.example.newsfeedproject.common.security.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * 클라이언트가 요청 시 보내는 JWT를 검증하여 사용자를 인증하고,
 * 인증된 사용자의 정보를 SecurityContextHolder에 저장한다.
 * ✅ 사용 시기: 로그인 이후 인증이 필요한 API 요청 시 작동
 *
 * @author jiwon jung
 */
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String token = null;

        String authorizationHeader = request.getHeader("Authorization");

        // 회원가입 or 로그인인지 확인(이 둘은 JWT 검사할 필요 x)
        if (requestURI.equals("/login") || requestURI.equals("/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰 있는지 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("JWT 토큰이 필요합니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다.");
            return;
        }

        // 있으면 가져옴("Bearer " 떼고)
        token = authorizationHeader.substring(7);

        // 가져오고 나서 Secret Key는 내가 만든게 맞는지 검증, 만료 기간 지났는지 검증
        if (!jwtUtil.validateToken(token)) {
            log.error("토큰이 유효하지 않습니다.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        // token 소멸 시간 검증(유효 시간이 만료된 경우)
        if (jwtUtil.isTokenExpired(token)) {
            log.error("토큰 유효 기간이 만료 되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 최종적으로 token 검증 완료 -> 일시적인 session 생성
        String email = jwtUtil.getEmail(token);

        // 인증 완료된 유저 정보 가져오기
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // credentials: 로그인 후 비밀번호 들고 있을 이유 x -> 보안상 null
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, Collections.emptyList());

        // 인증된 사용자의 정보를 안전하게 저장 및 관리하기 위해 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        // 요청을 컨트롤러로 전달
        filterChain.doFilter(request, response);
    }
}
