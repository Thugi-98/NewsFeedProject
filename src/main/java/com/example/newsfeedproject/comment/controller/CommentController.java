package com.example.newsfeedproject.comment.controller;

import com.example.newsfeedproject.comment.dto.CommentResponse;
import com.example.newsfeedproject.comment.dto.CreateCommentRequest;
import com.example.newsfeedproject.comment.dto.UpdateCommentRequest;
import com.example.newsfeedproject.comment.service.CommentService;
import com.example.newsfeedproject.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@Valid @RequestBody CreateCommentRequest request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentService.createComment(request)));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPostId(@PathVariable Long postId) {

        List<CommentResponse> comments = commentService.getCommentByPostId(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(comments));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(@PathVariable Long id, @Valid @RequestBody UpdateCommentRequest request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(commentService.updateComment(id,request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }
}
