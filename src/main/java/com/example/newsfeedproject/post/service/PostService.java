package com.example.newsfeedproject.post.service;

import com.example.newsfeedproject.common.dto.ApiResponse;
import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.post.dto.*;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
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
    //속성
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //기능
    public CreatePostResponse save(CreatePostRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        Post post = new Post(request.getTitle(), request.getContent(), user);
        Post savedPost = postRepository.save(post);

        return new CreatePostResponse(
                savedPost.getId(),
                savedPost.getUser().getName(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreatedAt(),
                savedPost.getModifiedAt()
        );
    }

    public Page<GetPostsResponse> getPosts(Pageable pageable, Long userId) {
        PageRequest request = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Post> posts;
        if (userId != null) {
            posts = postRepository.findByUserId(userId, request);
        } else {
            posts = postRepository.findAll(request);
        }

        return posts.map(post -> new GetPostsResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        ));
    }

    public UpdatePostResponse update(UpdatePostRequest request, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        post.update(request.getTitle(), request.getContent());
        return new UpdatePostResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }

    public void delte(Long postId, DeletePostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        User user = post.getUser();
        if(!user.getPassword().equals(request.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }

        postRepository.delete(post);
    }
}
