package com.example.newsfeedproject.follow.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.follow.dto.CreateFollowResponse;
import com.example.newsfeedproject.follow.dto.ReadFollowResponse;
import com.example.newsfeedproject.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /* 다른 사람 팔로우 하기 */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateFollowResponse>> followApi(
            @RequestParam Long targetId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(followService.create(user, targetId)));
    }

    /* userId를 통해 팔로우 목록 확인하기 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadFollowResponse>>> followingApi(
            @RequestParam(required = false) Long targetId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(followService.read(user, targetId)));
    }

    /* followId를 통해 언팔로우 하기 */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unfollowApi(
            @RequestParam Long targetId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        followService.delete(user, targetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}