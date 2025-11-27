package com.example.newsfeedproject.post.controller;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.post.dto.request.CreatePostRequest;
import com.example.newsfeedproject.post.dto.request.UpdatePostRequest;
import com.example.newsfeedproject.post.dto.response.CreatePostResponse;
import com.example.newsfeedproject.post.dto.response.GetPostResponse;
import com.example.newsfeedproject.post.dto.response.GetPostsResponse;
import com.example.newsfeedproject.post.dto.response.UpdatePostResponse;
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
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시물 생성 기능
     * @param request
     * @param user
     * @return
     */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CreatePostResponse>> createApi(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(postService.save(request, user)));
    }

    /**
     * 게시물 전체 조회 기능
     * @param pageable
     * @param userId
     * @param all
     * @return
     */
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<Page<GetPostsResponse>>> getPostsApi(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) Long userId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false, defaultValue = "false") boolean all,
            @RequestParam(required = false, defaultValue = "false") boolean onlyFollow
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(postService.getPosts(pageable, userId, user.getUsername(), all, onlyFollow)));
    }

    /**
     * 게시물 선택 조회 기능
     * @param id
     * @return
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<GetPostResponse>> getPostApi(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(postService.getPost(id)));
    }

    /**
     * 게시물 수정 기능
     * @param request
     * @param postId
     * @param user
     * @return
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updateApi(
            @Valid @RequestBody UpdatePostRequest request,
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(postService.update(request, postId, user)));
    }

    /**
     * 게시물 삭제 기능
     * @param user
     * @param postId
     * @return
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteApi(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long postId) {

        postService.delete(postId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
