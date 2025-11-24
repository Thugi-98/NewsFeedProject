package com.example.newsfeedproject.post.service;

import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.post.dto.CreatePostRequest;
import com.example.newsfeedproject.post.dto.CreatePostResponse;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        User user =  userRepository.findById(userId).orElseThrow(
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

}
