package com.example.newsfeedproject.like.postLike.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.like.postLike.dto.CreatePostLikeResponse;
import com.example.newsfeedproject.like.postLike.dto.ReadPostLikeResponse;
import com.example.newsfeedproject.like.postLike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes/posts")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    /* 게시물에 좋아요 누르기 */
    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<CreatePostLikeResponse>> likeApi(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(postLikeService.create(user, postId)));
    }

    /* postId를 통해 좋아요 목록 확인하기*/
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<ReadPostLikeResponse>>> readLikeApi(
            @PathVariable Long postId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(postLikeService.read(postId)));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> unlikeApi(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        postLikeService.delete(postId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
