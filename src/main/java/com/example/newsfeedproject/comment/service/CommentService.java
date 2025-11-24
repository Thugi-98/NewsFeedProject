package com.example.newsfeedproject.comment.service;

import com.example.newsfeedproject.comment.dto.CommentResponse;
import com.example.newsfeedproject.comment.dto.CreateCommentRequest;
import com.example.newsfeedproject.comment.dto.UpdateCommentRequest;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.entity.Comment;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.post.service.PostService;
import com.example.newsfeedproject.user.repository.UserRepository;
import com.example.newsfeedproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(CreateCommentRequest request) {

        User user = userRepository.findUserById(request.getUserId());

        Post post = postRepository.findPostById(request.getPostId());

        Comment comment = request.toEntity(user, post);
        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.from(savedComment);
    }

    // 댓글 조회
    @Transactional
    public List<CommentResponse> getCommentByPostId(Long postid) {

        postRepository.findById(postid);

        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postid);

        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long id, UpdateCommentRequest request) {

        Comment comment = findCommentById(id);

        comment.update(request.getComment());

        return CommentResponse.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {

        Comment comment = findCommentById(id);

        commentRepository.delete(comment);
    }

    public Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    return new CustomException(ErrorCode.NOT_FOUND_COMMENT);
                });
    }
}
