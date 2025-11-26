package com.example.newsfeedproject.common.config;

import com.example.newsfeedproject.common.security.jwt.*;
import com.example.newsfeedproject.common.security.user.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Spring Security 설정 클래스
 *
 * 주요 기능
 * 1. CORS 설정 - 프론트엔드와의 통신을 허용
 * 2. JWT 기반 인증/인가 - 기존의 세션 방식이 아닌 JWT를 사용해서 인증
 * 3. 엔드포인트별 권한 설정 - /admin 페이지는 ADMIN 권한이 있는 사용자만 접근 가능
 * 4. JWT 필터 - 인증 요청 시 JWT를 검증하여 인증 처리
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * UserDetailsService + PasswordEncoder 연걸해서 AuthenticationManager가 기본적으로 PasswordEncoder를 알게함
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;

    }

    /**
     * Spring Security의 AuthenticationManger를 Bean으로 등록
     * 로그인 시 사용자의 인증을 담당한다.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security 필터 체인 설정(JWT 인증을 기반으로 한 보안 설정 적용)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) {

        // 로그인 필터 (JWT 발급 담당)
        LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtUtil);
        loginFilter.setFilterProcessesUrl("/login");

        // JWT 인증 필터 (매 요청마다 JWT 검증)
        JWTFilter jwtFilter = new JWTFilter(jwtUtil, userDetailsService);

        http
                .csrf(csrf -> csrf.disable()) // JWT 사용 시 CSRF 보호 비활성화
                .formLogin(form -> form.disable()) // 기본 로그인 폼 비활성화 (JWT 사용)
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화

                // 엔드포인트 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/login").permitAll() //  로그인, 회원가입은 누구나 접근 허용
                        .anyRequest().authenticated()) // 그 외의 요청은 인증된 사용자만 접근 가능

                // JWT 필터 추가 (기존 UsernamePasswordAuthenticationFilter 이전에 실행)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 로그인 필터 추가 (JWTFilter 실행 후 JWT 발급 처리)
                .addFilterAfter(loginFilter, UsernamePasswordAuthenticationFilter.class)

                // 세션을 사용하지 않음 (JWT 기반 인증이므로 STATELESS 모드 설정)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 로그아웃
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 처리할 URL
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // 로그아웃 성공 처리
                        }))
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        return http.build();
    }
}
