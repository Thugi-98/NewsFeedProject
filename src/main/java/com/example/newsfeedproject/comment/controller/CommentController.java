package com.example.newsfeedproject.comment.controller;

import com.example.newsfeedproject.comment.dto.response.CommentResponse;
import com.example.newsfeedproject.comment.dto.request.CreateCommentRequest;
import com.example.newsfeedproject.comment.dto.request.UpdateCommentRequest;
import com.example.newsfeedproject.comment.service.CommentService;
import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createCommentApi(@Valid @RequestBody CreateCommentRequest request, @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentService.createComment(request,user)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> readCommentApi(@RequestParam Long postId) {

        List<CommentResponse> comments = commentService.readComment(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(comments));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateCommentApi(@PathVariable Long id, @Valid @RequestBody UpdateCommentRequest request, @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentService.updateComment(id,request, user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCommentApi(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        commentService.deleteComment(id, user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }
}
