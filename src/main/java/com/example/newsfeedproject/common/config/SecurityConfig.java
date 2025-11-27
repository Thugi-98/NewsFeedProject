package com.example.newsfeedproject.common.config;

import com.example.newsfeedproject.common.security.jwt.*;
import com.example.newsfeedproject.common.security.user.CustomUserDetailsService;
import com.example.newsfeedproject.common.security.utils.JwtUtil;
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


// Spring Security 설정 클래스
// Spring Security는 스프링 기반 애플리케이션의 보안(인증, 권한)을 담당하는 프레임워크
// Spring Security는 필터 기반으로 동작하기 때문에 스프링 MVC와 분리되어 동작한다.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    // UserDetailsService + PasswordEncoder 연걸해서 AuthenticationManager가 기본적으로 PasswordEncoder를 알게함
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Spring Security의 AuthenticationManger를 Bean으로 등록
    // 로그인 시 사용자의 인증을 담당한다.
    @Bean
    public AuthenticationManager authenticationManager() {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 직렬화, 역직렬화를 위한 ObjectMapper 빈 등록
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // Spring Security 필터 체인 설정(JWT 인증을 기반으로 한 보안 설정 적용)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) {

        // JWT 인증 필터 (로그인 이후 매 요청마다 JWT 검증)
        JwtFilter jwtFilter = new JwtFilter(jwtUtil, userDetailsService, handlerExceptionResolver);

        http
                .csrf(csrf -> csrf.disable()) // JWT 사용 시 CSRF 보호 비활성화 (사용자가 의도하지 않은 작업을 특정 웹사이트에서 수행하도록 유도하는 공격 기법)
                .formLogin(form -> form.disable()) // 기본 로그인 폼 비활성화 (JWT 사용)
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화

                // 엔드포인트들의 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/login").permitAll() // 로그인, 회원가입은 누구나 요청 허용
                        .anyRequest().authenticated()) // 그 외의 요청은 인증된 사용자만 접근 가능

                // JWT 필터 추가 (기존 UsernamePasswordAuthenticationFilter 이전에 실행)
                // UsernamePasswordAuthenticationFilter -> 사용자의 아이디(username)와 비밀번호(password)를 받아 인증을 처리하는 필터
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 세션을 사용하지 않음 (JWT 기반 인증이므로 STATELESS 모드 설정)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Security 로그아웃 비활성화
                .logout(logout -> logout.disable());

        return http.build(); // 체인 빌드
    }
}
