package com.example.newsfeedproject.post.service;

import com.example.newsfeedproject.comment.dto.response.CommentResponse;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.common.entity.Comment;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.post.dto.request.PostCreateRequest;
import com.example.newsfeedproject.post.dto.request.PostUpdateRequest;
import com.example.newsfeedproject.post.dto.response.PostCreateResponse;
import com.example.newsfeedproject.post.dto.response.PostGetOneResponse;
import com.example.newsfeedproject.post.dto.response.PostGetAllResponse;
import com.example.newsfeedproject.post.dto.response.PostUpdateResponse;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // 게시물 생성 기능
    public PostCreateResponse save(PostCreateRequest request, CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Post post = new Post(request.getTitle(), request.getContent(), user);
        Post savedPost = postRepository.save(post);

        return PostCreateResponse.from(savedPost);
    }

    // 게시물 전체 조회 기능
    @Transactional(readOnly = true)
    public Page<PostGetAllResponse> getPosts(Pageable pageable, Long userId, boolean all) {

        // 페이징 없이 모든 게시물을 보기위한 조회
        PageRequest request;
        if (all) {
            request = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            request = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }

        // 페이징 조회 시 유저 아이디의 관련된 게시물만 조회 외에는 모든 게시물을 페이징 조회
        Page<Post> posts;
        if (userId != null) {
            posts = postRepository.findByUserIdAndIsDeletedFalse(userId, request);
        } else {
            posts = postRepository.findByIsDeletedFalse(request);
        }

        return posts.map(post -> {
            long count = commentRepository.countByPostId(post.getId());
            return PostGetAllResponse.from(post, count);
        });
    }

    // 게시물 단건 조회 기능
    @Transactional(readOnly = true)
    public PostGetOneResponse getPost(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(post.getId());

        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from)
                .toList();

        return PostGetOneResponse.from(post, commentResponses);
    }

    // 게시물 수정 기능
    public PostUpdateResponse update(PostUpdateRequest request, Long postId, CustomUserDetails userDetails) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 본인 말고 다른 유저의 게시물은 삭제 불가
        User user = post.getUser();
        if (!user.getEmail().equals(userDetails.getUserEmail())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        post.update(request.getTitle(), request.getContent());
        postRepository.flush();

        return PostUpdateResponse.from(post);
    }

    // 게시물 삭제 기능
    public void delete(Long postId, CustomUserDetails userDetails) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 본인 말고 다른 유저의 게시물은 삭제 불가
        User user = post.getUser();
        if (!user.getEmail().equals(userDetails.getUserEmail())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        post.softDelete();
    }
}
