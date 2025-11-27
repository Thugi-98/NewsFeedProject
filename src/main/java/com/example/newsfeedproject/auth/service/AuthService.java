package com.example.newsfeedproject.auth.service;

import com.example.newsfeedproject.auth.dto.request.LoginUserRequest;
import com.example.newsfeedproject.auth.dto.response.TokenResponse;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.auth.dto.request.SignupUserRequest;
import com.example.newsfeedproject.auth.dto.response.SignupUserResponse;
import com.example.newsfeedproject.common.security.utils.JwtUtil;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 인증의 핵심 비즈니스 로직을 담당하는 클래스
 *
 * @author jiwon jung
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입을 합니다.
     */
    @Transactional
    public SignupUserResponse signup(SignupUserRequest request) {

        // 이미 가입된 이메일일 경우 예외 발생
        if (userRepository.existsByEmail((request.getEmail()))) {
            log.error("이미 가입된 이메일입니다. {}", ErrorCode.DUPLICATE_EMAIL);
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getEmail(),
                encodedPassword,
                request.getBirth(),
                request.getIntroduction()
        );

        userRepository.save(user);

        return SignupUserResponse.from(user);
    }

    /**
     * 로그인을 합니다.
     */
    @Transactional
    public TokenResponse login(LoginUserRequest request) {

        // 요청 이메일로 가입된 유저가 있는지 확인
        User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.LOGIN_FAIL)
        );

        // 비밀번호가 일치하는지 확인
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }

        // JWT 토큰 발급
        String token = jwtUtil.createJwt(user.getEmail());

        return new TokenResponse(token);
    }
}
