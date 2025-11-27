package com.example.newsfeedproject.post.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.post.dto.request.PostCreateRequest;
import com.example.newsfeedproject.post.dto.request.PostUpdateRequest;
import com.example.newsfeedproject.post.dto.response.PostCreateResponse;
import com.example.newsfeedproject.post.dto.response.PostGetOneResponse;
import com.example.newsfeedproject.post.dto.response.PostGetAllResponse;
import com.example.newsfeedproject.post.dto.response.PostUpdateResponse;
import com.example.newsfeedproject.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


     // 게시물 생성 기능
    @PostMapping
    public ResponseEntity<ApiResponse<PostCreateResponse>> createPostApi(
            @Valid @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(postService.save(request, user)));
    }

    // 게시물 전체조회 기능
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostGetAllResponse>>> getAllPostApi(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) Long userId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false, defaultValue = "false") boolean all,
            @RequestParam(required = false, defaultValue = "false") boolean onlyFollow
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(postService.getPosts(pageable, userId, user.getUserEmail(), all, onlyFollow)));
    }

    // 게시물 단건 조회 기능
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostGetOneResponse>> getOnePostApi(
            @PathVariable Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(postService.getPost(id)));
    }

    // 게시물 수정 기능
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostUpdateResponse>> updatePostApi(
            @Valid @RequestBody PostUpdateRequest request,
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(postService.update(request, id, user)));
    }

    // 게시물 삭제 기능
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePostApi(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id)
    {
        postService.delete(id, user);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
