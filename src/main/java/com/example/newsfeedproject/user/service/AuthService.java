package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.DuplicatedUserException;
import com.example.newsfeedproject.user.dto.request.CreateUserRequest;
import com.example.newsfeedproject.user.dto.response.CreateUserResponse;
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

    /**
     * 회원가입을 합니다.
     */
    @Transactional
    public CreateUserResponse signup(CreateUserRequest request) {

        // 이미 가입된 이메일일 경우 예외 발생
        if (userRepository.existsByEmail((request.getEmail()))) {
            log.error("이미 가입된 이메일입니다. {}", ErrorCode.DUPLICATE_EMAIL);
            throw new DuplicatedUserException(ErrorCode.DUPLICATE_EMAIL);
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

        return CreateUserResponse.from(user);
    }
}
