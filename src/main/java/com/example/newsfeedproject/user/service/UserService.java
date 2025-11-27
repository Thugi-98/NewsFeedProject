package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.user.dto.request.UserDeleteRequest;
import com.example.newsfeedproject.user.dto.request.UserUpdateRequest;
import com.example.newsfeedproject.user.dto.response.UserGetAllResponse;
import com.example.newsfeedproject.user.dto.response.UserGetOneResponse;
import com.example.newsfeedproject.user.dto.response.UserUpdateResponse;
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


    // 유저 ID로 선택 조회
    @Transactional(readOnly = true)
    public UserGetOneResponse getOne(Long userId) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return UserGetOneResponse.from(user);
    }


    // 유저 전체 조회 (및 이름으로 조회) 통합
    @Transactional(readOnly = true)
    public List<UserGetAllResponse> getAllUser(String name) {

        List<User> users;

        if (name != null && !name.trim().isEmpty()) {
            users = userRepository.findByNameAndIsDeletedFalse(name);
        } else {
            users = userRepository.findAll().stream()
                    .filter(user -> !user.isDeleted())
                    .collect(Collectors.toList());
        }

        return users.stream()
                .map(UserGetAllResponse::from)
                .collect(Collectors.toList());
    }


    // 유저 수정
    public UserUpdateResponse updateUser(Long userId, UserUpdateRequest request, CustomUserDetails userDetails) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!user.getEmail().equals(userDetails.getUserEmail())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String newEncodedPassword = null;

        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {

            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
            }

            newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        }

        user.update(request, newEncodedPassword);
        userRepository.flush();

        return UserUpdateResponse.from(user);
    }


    // 유저 삭제
    public void deleteUser(UserDeleteRequest request, CustomUserDetails userDetails) {

        String userEmail = userDetails.getUserEmail();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        user.softDelete();
    }
}