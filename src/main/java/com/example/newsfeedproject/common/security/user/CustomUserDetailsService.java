package com.example.newsfeedproject.common.security.user;

import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService를 구현한 사용자 정보를 찾아주는 서비스 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * email을 이용해 사용자 정보를 조회하고 사용자 정보 DTO 반환(부모 타입 반환 - 다형성)
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow( // 실질적 DB 조회
                () -> new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getMessage())
        );

        // Spring Security가 이해하는 UserDetails(일종의 DTO)로 변환해서 리턴
        return new CustomUserDetails(user);
    }

}
