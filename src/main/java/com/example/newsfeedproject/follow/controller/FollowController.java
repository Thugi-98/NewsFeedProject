package com.example.newsfeedproject.follow.controller;

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
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /* 다른 사람 팔로우 하기 */
    @PostMapping("/follows")
    public ResponseEntity<CreateFollowResponse> createFollow(
            @RequestBody CreateFollowRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(followService.create(request));
    }

    /* userId를 통해 팔로우 목록 확인하기 */
    @GetMapping("/follows")
    public ResponseEntity<List<ReadFollowResponse>> readFollow(
            @RequestParam Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(followService.read(userId));
    }

    /* followId를 통해 언팔로우 하기 */
    @DeleteMapping("/follows/{id}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable Long id
    ) {
        followService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
