package com.example.newsfeedproject.comment.controller;

import com.example.newsfeedproject.comment.dto.request.CommentCreateRequest;
import com.example.newsfeedproject.comment.dto.response.CommentCreateResponse;
import com.example.newsfeedproject.comment.dto.request.CommentUpdateRequest;
import com.example.newsfeedproject.comment.dto.response.CommentGetAllResponse;
import com.example.newsfeedproject.comment.dto.response.CommentUpdateResponse;
import com.example.newsfeedproject.comment.service.CommentService;
import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성 API
    @PostMapping
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createCommentApi(
            @RequestParam Long postId,
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody CommentCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentService.createComment(postId, user, request)));
    }

    // 툭정 게시글의 댓글 전체 조회 API
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentGetAllResponse>>> getAllCommentApi(
            @RequestParam Long postId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentService.getComment(postId)));
    }

    // 댓글 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateCommentApi(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentService.updateComment(id, request, user)));
    }

    // 댓글 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCommentApi(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user) {

        commentService.deleteComment(id, user);

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }
}
