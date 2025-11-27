package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.user.dto.UserDto;
import com.example.newsfeedproject.user.dto.request.DeleteUserRequest;
import com.example.newsfeedproject.user.dto.request.UpdateUserRequest;
import com.example.newsfeedproject.user.dto.response.ReadUserResponse;
import com.example.newsfeedproject.user.dto.response.UpdateUserResponse;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    // 유저 이름으로 조회
    public List<ReadUserResponse> findUserByName(String name) {
        // 1. 이름으로 유저 목록 조회하기
        List<User> users = userRepository.findByName(name);

        // 2. User 엔터티 목록을 DTO 목록으로 변환
        if(users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(ReadUserResponse::from)
                .collect(Collectors.toList());
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
    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request, CustomUserDetails userDetails) {

        // 1. 유저 찾기
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 2. 다른 사람 정보를 수정할 수 없게 하는 기능
        if(!user.getEmail().equals(userDetails.getUsername())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        // 3. 기존 비밀번호 일치 확인 (본인 인증)
        // 입력된 현재 비밀번호와 유저에 저장된 암호화된 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 4. 정보 수정 (비밀번호 수정값 유무에 따라)
        String newEncodedPassword = null;
        // 4-1. 비밀번호를 변경하는 경우
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            // 4-1-1. 현재 비밀번호와 동일한 비밀번호로 수정하는 경우 방지
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
            }
            // 4-1-2. 새로운 비밀번호를 암호화하여 준비
            newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        }

        // 4-2. 수정 정보 업데이트
        user.update(request, newEncodedPassword);

        UserDto dto = UserDto.from(user);
        return UpdateUserResponse.from(dto);
    }

    // 유저 삭제
    public void deleteUser(DeleteUserRequest request, CustomUserDetails userDetails) {

        // 1. 유저 찾기 (삭제 여부와 관계없이 조회)
        String userEmail = userDetails.getUsername();

        User user = userRepository.findByEmailWithDeleted(userEmail).orElseThrow(
                () ->  new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        // 2. 기존 비밀번호 일치 확인 (본인 인증 유지)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 3. 유저 탈퇴 (소프트 삭제)
        user.softDelete();
    }


}