package com.example.newsfeedproject.like.commentLike.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.like.commentLike.dto.CreateCommentLikeResponse;
import com.example.newsfeedproject.like.commentLike.dto.ReadCommentLikeResponse;
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

    /* 게시물에 좋아요 누르기 */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateCommentLikeResponse>> likeApi(
            @RequestParam Long commentId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(commentLikeService.create(user, commentId)));
    }

    /* postId를 통해 좋아요 목록 확인하기*/
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadCommentLikeResponse>>> readLikeApi(
            @RequestParam Long commentId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(commentLikeService.read(commentId)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlikeApi(
            @RequestParam Long commentId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentLikeService.delete(commentId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
