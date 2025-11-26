package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.user.dto.request.UpdateUserRequest;
import com.example.newsfeedproject.user.dto.response.ReadUserResponse;
import com.example.newsfeedproject.user.dto.response.UpdateUserResponse;
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

    // 유저 조회 (선택 조회)
    @GetMapping("/{Id}")
    public ResponseEntity<ApiResponse<ReadUserResponse>> readUserApi(@PathVariable Long Id) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(userService.readUser(Id)));
    }

    @GetMapping("/findUser")
    public ResponseEntity<ApiResponse<List<ReadUserResponse>>> readUserByNameApi(@RequestParam("name") String name) {
        List<ReadUserResponse> users = userService.findUserByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(users));
    }

    // 유저 조회 (전체 조회)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadUserResponse>>> readUsersApi() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(userService.readUsers()));
    }

    // 유저 수정
    @PutMapping("/{Id}")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateUserApi(@PathVariable Long Id,
                                                                         @Valid @RequestBody UpdateUserRequest request,
                                                                         @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(userService.updateUser(Id, request, user)));
    }

    // 유저 삭제
    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> deleteUserApi(@PathVariable Long Id,
                                              @AuthenticationPrincipal CustomUserDetails user) {
        userService.deleteUser(Id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
