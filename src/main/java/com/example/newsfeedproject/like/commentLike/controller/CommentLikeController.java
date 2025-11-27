package com.example.newsfeedproject.like.commentLike.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.like.commentLike.dto.CommentLikeCreateResponse;
import com.example.newsfeedproject.like.commentLike.dto.CommentLikeGetAllByCommentResponse;
import com.example.newsfeedproject.like.commentLike.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes/comments")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 댓글에 좋아요 누르기
    @PostMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentLikeCreateResponse>> likeApi(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(commentLikeService.create(user, commentId)));
    }

    // commentId를 통해 좋아요 목록 확인하기
    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<List<CommentLikeGetAllByCommentResponse>>> readLikeApi(
            @PathVariable Long commentId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentLikeService.read(commentId)));
    }

    // 댓글에 좋아요 취소하기
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> unlikeApi(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentLikeService.delete(commentId, user);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
