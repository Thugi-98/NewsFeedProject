package com.example.newsfeedproject.comment.service;

import com.example.newsfeedproject.comment.dto.request.CreateCommentRequest;
import com.example.newsfeedproject.comment.dto.response.CommentResponse;
import com.example.newsfeedproject.comment.dto.request.UpdateCommentRequest;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.common.entity.Comment;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(Long postId, CustomUserDetails userDetails,  CreateCommentRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NOT_FOUND_USER)
                );

        Post post = postRepository.findById(postId)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );
        if (post.isDeleted()) {
            throw new CustomException((ErrorCode.NOT_FOUND_POST));
        }

        Comment comment = request.toEntity(user, post);
        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.from(savedComment);
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> readComment(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );
        if (post.isDeleted()) {
            throw new CustomException((ErrorCode.NOT_FOUND_POST));
        }

        List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(postId);

        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long id, UpdateCommentRequest request, CustomUserDetails userDetails) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        User user = comment.getUser();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        comment.update(request.getComment());

        return CommentResponse.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id, CustomUserDetails userDetails) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        User user = comment.getUser();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        comment.softDelete();
    }
}
