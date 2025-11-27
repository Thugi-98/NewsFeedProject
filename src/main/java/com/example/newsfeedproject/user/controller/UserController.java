package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.user.dto.request.UserDeleteRequest;
import com.example.newsfeedproject.user.dto.request.UserUpdateRequest;
import com.example.newsfeedproject.user.dto.response.UserGetAllResponse;
import com.example.newsfeedproject.user.dto.response.UserGetOneResponse;
import com.example.newsfeedproject.user.dto.response.UserUpdateResponse;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")  // 베이스 경로
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // 유저 ID로 선택 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserGetOneResponse>> getUserApi(
            @PathVariable Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        userService.getOne(id)));

    }


    // 유저 전체 조회 (및 이름으로 조회) 통합
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserGetAllResponse>>> getAllUserApi(
            @RequestParam(value = "name", required = false) String name) {

        List<UserGetAllResponse> users = userService.getAllUser(name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(users));

    }


    // 유저 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUserApi(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        userService.updateUser(id, request, user)));

    }


    // 유저 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteUserApi(
            @Valid @RequestBody UserDeleteRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        userService.deleteUser(request, userDetails);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();

    }
}
