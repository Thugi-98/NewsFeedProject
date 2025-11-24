package com.example.newsfeedproject.auth.service;

import com.example.newsfeedproject.auth.dto.*;
import com.example.newsfeedproject.common.config.PasswordEncoder;
import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.DuplicatedUserException;
import com.example.newsfeedproject.common.exception.InvalidPasswordException;
import com.example.newsfeedproject.common.exception.UserNotFoundException;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입을 합니다.
     */
    @Transactional
    public CreateUserResponse signup(CreateUserRequest request) {

        // 이미 가입된 이메일일 경우 예외 발생
        if (userRepository.existsByEmail((request.getEmail()))) {
            log.error("이미 가입된 이메일 {}", ErrorCode.DUPLICATE_EMAIL);
            throw new DuplicatedUserException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getEmail(),
                encodedPassword,
                request.getBirth(),
                request.getIntroduce()
        );

        userRepository.save(user);

        return CreateUserResponse.from(user);
    }

    /**
     * 로그인을 합니다.
     */
    @Transactional(readOnly = true)
    public SessionUser login(LoginUserRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> {
                    log.error("존재하지 않는 이메일 {}", ErrorCode.NOT_FOUND_USER);
                    return new UserNotFoundException(ErrorCode.NOT_FOUND_USER); // 해당 이메일로 가입된 유저가 존재하지 않을 시 예외 발생
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) { // 로그인 요청 시 입력한 비밀번호와 DB에 저장된 비밀번호가 같은지 검증
            log.error("비밀번호 불일치 {}", ErrorCode.LOGIN_FAIL);
            throw new InvalidPasswordException(ErrorCode.LOGIN_FAIL);
        }

        return SessionUser.from(user);
    }
}
