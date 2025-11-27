package com.example.newsfeedproject.common.config;

import com.example.newsfeedproject.common.security.jwt.*;
import com.example.newsfeedproject.common.security.user.CustomUserDetailsService;
import com.example.newsfeedproject.common.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import tools.jackson.databind.ObjectMapper;


/**
 * Spring Security 설정 클래스
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

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
     * 직렬화, 역직렬화를 위한 ObjectMapper 빈 등록
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Spring Security 필터 체인 설정(JWT 인증을 기반으로 한 보안 설정 적용)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) {

        // 로그인 필터 (JWT 발급 담당)
        // 로그인 시도를 가로채서 인증하고, 성공 시 JWT 발급하도록 하는 커스텀 필터
        LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtUtil, objectMapper(), handlerExceptionResolver);
        loginFilter.setFilterProcessesUrl("/login"); // 해당 필터가 /login 요청을 처리하도록 지정

        // JWT 인증 필터 (로그인 이후 매 요청마다 JWT 검증)
        JwtFilter jwtFilter = new JwtFilter(jwtUtil, userDetailsService, handlerExceptionResolver);

        http
                .csrf(csrf -> csrf.disable()) // JWT 사용 시 CSRF 보호 비활성화
                .formLogin(form -> form.disable()) // 기본 로그인 폼 비활성화 (JWT 사용)
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화

                // 엔드포인트들의 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/login").permitAll() // 로그인, 회원가입은 누구나 요청 허용
                        .anyRequest().authenticated()) // 그 외의 요청은 인증된 사용자만 접근 가능

                // LoginFilter가 UserNamePasswordAuthenticationFilter를 대체
                // UsernamePasswordAuthenticationFilter -> 사용자의 아이디(username)와 비밀번호(password)를 받아 인증을 처리하는 필터
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)

                // JWT 필터 추가 (기존 UsernamePasswordAuthenticationFilter 이전에 실행)
                // UsernamePasswordAuthenticationFilter -> 사용자의 아이디(username)와 비밀번호(password)를 받아 인증을 처리하는 필터
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 세션을 사용하지 않음 (JWT 기반 인증이므로 STATELESS 모드 설정)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 로그아웃
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 처리할 URL
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // 로그아웃 성공 처리
                        }));

                // 인증 실패 시 동작하는 엔트리 포인트 지정
//                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        return http.build(); // 체인 빌드
    }
}
