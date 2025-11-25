package com.example.newsfeedproject.post.service;

import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.post.dto.*;
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
     * 일정 생성 기능
     * @param request
     * @return
     */
    public CreatePostResponse save(CreatePostRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
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

    /**
     * 일정 조회 기능
     * @param pageable
     * @param userId
     * @param all
     * @return
     */
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

        return posts.map(post -> new GetPostsResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        ));
    }

    /**
     * 일정 수정 기능
     * @param request
     * @param postId
     * @return
     */
    public UpdatePostResponse update(UpdatePostRequest request, Long postId) {
        Post post = postRepository.findByIdAndIsDeleteFalse(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        User user = post.getUser();
        if (!user.getId().equals(request.getUserId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

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

    /**
     * 일정 삭제 기능
     * @param postId
     * @param request
     */
    public void delete(Long postId, DeletePostRequest request) {
        Post post = postRepository.findByIdAndIsDeleteFalse(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        User user = post.getUser();
        if (!user.getId().equals(request.getUserId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        post.softDelete();
    }
}
