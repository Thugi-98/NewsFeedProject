package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.config.PasswordEncoder;
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
                .map(UserDto::from)
                .map(ReadUserResponse::from)
                .collect(Collectors.toList());
    }

    // 유저 수정
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest request) {
        // 1. 유저 찾기
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 2. 비밀번호 수정 로직
        // - 현재 비밀번호 변수 : currentPassword / 새로운 비밀번호 변수: password
        // - request.getPassword() : 새로운 비밀번호
        // - 새로운 비밀번호가 null이 아니거나 (입력되었거나) 빈 문자열이 아닌 경우에만,'비밀번호 수정' 로직을 실행
        // - 즉, 이름, 생일 등 다른 정보만 수정하고 싶을 때는 이 블록이 건너뛰어짐

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {

            // 2-1. 현재 비밀번호 일치 확인 (본인 인증)
            // 입력된 비밀번호와 유저에 저장된 비밀번호가 일치하지 않는다면 에러 발생
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
            }

            // 2-2. 현재 비밀번호와 동일한 비밀번호로 수정하는 경우 방지
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
            }

            // 2-3. 세 비밀번호 암호화 및 업데이트
            // - 이름, 생일, 소개 등 비밀번호 외의 필드를 업데이트
            // - 비밀번호를 수정하지 않은 경우(if 블록을 건너뛴 경우)
            String newEncodedPassword = passwordEncoder.encode(request.getPassword());
            user.updatePassword(newEncodedPassword);

        }
        // 3. 일반 정보 수정
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
