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

    private final PostService postService;

    /**
     * 일정 생성 기능
     * @param request
     * @return
     */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPostApi(@RequestBody CreatePostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(postService.save(request)));
    }

    /**
     * 일정 조회 기능
     * @param pageable
     * @param userId
     * @param all
     * @return
     */
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<Page<GetPostsResponse>>> getPostApi(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean all) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(postService.getPosts(pageable, userId, all)));
    }

    /**
     * 일정 수정 기능
     * @param request
     * @param postId
     * @return
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePostApi(
            @RequestBody UpdatePostRequest request,
            @PathVariable Long postId) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(postService.update(request, postId)));
    }

    /**
     * 일정 삭제 기능
     * @param request
     * @param postId
     * @return
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePostApi(
            @RequestBody DeletePostRequest request,
            @PathVariable Long postId) {

        postService.delete(postId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
