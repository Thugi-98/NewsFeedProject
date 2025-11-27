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

// Spring Security에서 유저의 정보를 가져오는 클래스
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // email을 이용해 사용자 정보를 조회하고 사용자 정보 DTO 반환(부모 타입 반환 - 다형성)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
                    log.error("[Not Found User] 유저를 찾을 수 없습니다.");
                    return new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getMessage());
                });

        // Spring Security가 이해하는 UserDetails(일종의 DTO)로 변환해서 리턴
        return new CustomUserDetails(user);
    }
}
