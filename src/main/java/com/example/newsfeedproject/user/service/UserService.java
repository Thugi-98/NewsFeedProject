package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.user.dto.UserDto;
import com.example.newsfeedproject.user.dto.request.CreateUserRequest;
import com.example.newsfeedproject.user.dto.request.UpdateUserRequest;
import com.example.newsfeedproject.user.dto.response.CreateUserResponse;
import com.example.newsfeedproject.user.dto.response.ReadUserResponse;
import com.example.newsfeedproject.user.dto.response.UpdateUserResponse;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 생성 (임의)
    public CreateUserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getBirth(),
                request.getIntroduction());

        User savedUser = userRepository.save(user);

        return new CreateUserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getBirth(),
                savedUser.getIntroduction(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
    }


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
                .map(UserDto :: from)
                .map(ReadUserResponse :: from)
                .collect(Collectors.toList());
    }

    // 유저 수정
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 비밀번호 수정 조건
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {

            // 1. 현재 비밀번호 일치 확인 (불일치 에러)
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.LOGIN_FAIL);
            }

            // 2. 현재 비밀번호와 동일한 비밀번호로 수정하는 경우
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.LOGIN_FAIL);
            }

            // 3. 새 비밀번호 암호화
            String newEncodedPassword = passwordEncoder.encode(request.getPassword());
            user.updatePassword(newEncodedPassword);
        }
        user.update(request);

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
