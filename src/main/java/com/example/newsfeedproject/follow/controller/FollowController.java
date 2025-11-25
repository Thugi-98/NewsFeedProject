package com.example.newsfeedproject.follow.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.follow.dto.CreateFollowRequest;
import com.example.newsfeedproject.follow.dto.CreateFollowResponse;
import com.example.newsfeedproject.follow.dto.ReadFollowResponse;
import com.example.newsfeedproject.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestBody CreateFollowRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(followService.create(request)));
    }

    /* userId를 통해 팔로우 목록 확인하기 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadFollowResponse>>> followingApi(
            @RequestParam Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(followService.read(userId)));
    }

    /* followId를 통해 언팔로우 하기 */
    @DeleteMapping
    public ResponseEntity<Void> unfollowApi(
            @RequestParam Long userId,
            @RequestParam Long targetId
    ) {
        followService.delete(userId, targetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}