package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.user.dto.request.UpdateUserRequest;
import com.example.newsfeedproject.user.dto.response.ReadUserResponse;
import com.example.newsfeedproject.user.dto.response.UpdateUserResponse;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")  // 베이스 경로
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 조회 (선택 조회)
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ReadUserResponse>> readUserApi(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(userService.readUser(userId)));
    }

    // 유저 조회 (전체 조회)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadUserResponse>>> readUsersApi() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(userService.readUsers()));
    }

    // 유저 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateUserApi(@Valid @PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(userService.updateUser(userId, request)));
    }

    // 유저 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserApi(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
