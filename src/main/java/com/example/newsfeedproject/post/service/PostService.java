package com.example.newsfeedproject.post.service;

import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.post.dto.request.CreatePostRequest;
import com.example.newsfeedproject.post.dto.request.UpdatePostRequest;
import com.example.newsfeedproject.post.dto.response.CreatePostResponse;
import com.example.newsfeedproject.post.dto.response.GetPostResponse;
import com.example.newsfeedproject.post.dto.response.GetPostsResponse;
import com.example.newsfeedproject.post.dto.response.UpdatePostResponse;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시물 생성 기능
     * @param request
     * @param userDetails
     * @return
     */
    public CreatePostResponse save(CreatePostRequest request, CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        Post post = new Post(request.getTitle(), request.getContent(), user);
        Post savedPost = postRepository.save(post);

        return CreatePostResponse.from(savedPost);
    }

    /**
     * 게시물 전체 조회 기능
     * @param pageable
     * @param userId
     * @param all
     * @return
     */
    @Transactional(readOnly = true)
    public Page<GetPostsResponse> getPosts(Pageable pageable, Long userId, boolean all) {

        PageRequest request;
        if (all) {
            request = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            request = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }


        Page<Post> posts;
        if (userId != null) {
            posts = postRepository.findByUserIdAndIsDeleteFalse(userId, request);
        } else {
            posts = postRepository.findByIsDeleteFalse(request);
        }

        return posts.map(GetPostsResponse::from);
    }

    /**
     * 게시물 단건 조회 기능
     * @param id
     * @return
     */
    public GetPostResponse getPost(Long id) {
        Post post = postRepository.findByIdAndIsDeleteFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        return GetPostResponse.from(post);
    }

    /**
     * 일정 수정 기능
     * @param request
     * @param postId
     * @return
     */
    public UpdatePostResponse update(UpdatePostRequest request, Long postId, CustomUserDetails userDetails) {
        Post post = postRepository.findByIdAndIsDeleteFalse(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        User user = post.getUser();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        post.update(request.getTitle(), request.getContent());

        return UpdatePostResponse.from(post);
    }

    /**
     * 게시물 삭제 기능
     * @param postId
     * @param userDetails
     */
    public void delete(Long postId, CustomUserDetails userDetails) {
        Post post = postRepository.findByIdAndIsDeleteFalse(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        User user = post.getUser();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        post.softDelete();
    }
}
