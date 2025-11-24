package com.example.newsfeedproject.post.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.post.dto.*;
import com.example.newsfeedproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {
    //속성
    private final PostService postService;

    //기능
    //게시물 생성
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CreatePostResponse>> create(@RequestBody CreatePostRequest request, Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(postService.save(request, userId)));
    }

    //게시물 조회
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<Page<GetPostsResponse>>> getPost(@PageableDefault(size = 10) Pageable pageable,
                                                                       @RequestParam(required = false) Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(postService.getPosts(pageable, userId)));
    }

    //게시물 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> update(@RequestBody UpdatePostRequest request,
                                                                  @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(postService.update(request, postId)));
    }

    //게시물 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> delete(@RequestBody DeletePostRequest request,
                                                    @PathVariable Long postId) {

        postService.delte(postId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
