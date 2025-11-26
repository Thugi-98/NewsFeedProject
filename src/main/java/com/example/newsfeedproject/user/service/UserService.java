package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.user.dto.UserDto;
import com.example.newsfeedproject.user.dto.request.UpdateUserRequest;
import com.example.newsfeedproject.user.dto.response.ReadUserResponse;
import com.example.newsfeedproject.user.dto.response.UpdateUserResponse;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 유저 조회 (선택 조회)
    @Transactional(readOnly = true)
    public ReadUserResponse readUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));
        UserDto dto = UserDto.from(user);

        return ReadUserResponse.from(dto);
    }

    // 유저 조회 (전체 조회)
    @Transactional(readOnly = true)
    public List<ReadUserResponse> readUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .map(ReadUserResponse::from)
                .collect(Collectors.toList());
    }

    // 유저 수정
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {

        // 1. 유저 찾기
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 2. 기존 비밀번호 일치 확인 (본인 인증)
        // 입력된 현재 비밀번호와 유저에 저장된 암호화된 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 3. 정보 수정 (비밀번호 수정값 유무에 따라)
        String newEncodedPassword = null;
        // 3-1. 비밀번호를 변경하는 경우
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            // 3-1-1. 현재 비밀번호와 동일한 비밀번호로 수정하는 경우 방지
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
            }
            // 3-1-2. 새로운 비밀번호를 암호화하여 준비
            newEncodedPassword = passwordEncoder.encode(request.getPassword());

        }

        // 3-2. 수정 정보 업데이트
        user.update(request, newEncodedPassword);

        UserDto dto = UserDto.from(user);
        return UpdateUserResponse.from(dto);
    }

    // 유저 삭제
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));
        userRepository.delete(user);
    }
}