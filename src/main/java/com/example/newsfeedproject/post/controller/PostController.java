package com.example.newsfeedproject.post.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.post.dto.CreatePostRequest;
import com.example.newsfeedproject.post.dto.CreatePostResponse;
import com.example.newsfeedproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    //속성
    private final PostService postService;

    //기능
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CreatePostResponse>> create (@RequestBody CreatePostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(request));
    }
}
