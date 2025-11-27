package com.example.newsfeedproject.comment.service;

import com.example.newsfeedproject.comment.dto.request.CommentCreateRequest;
import com.example.newsfeedproject.comment.dto.response.CommentCreateResponse;
import com.example.newsfeedproject.comment.dto.request.CommentUpdateRequest;
import com.example.newsfeedproject.comment.dto.response.CommentGetAllResponse;
import com.example.newsfeedproject.comment.dto.response.CommentUpdateResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    public CommentCreateResponse createComment(Long postId, CustomUserDetails userDetails, CommentCreateRequest request) {

        User findUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        if (findPost.isDeleted()) {
            throw new CustomException((ErrorCode.NOT_FOUND_POST));
        }

        Comment newComment = request.toEntity(findUser, findPost);
        Comment savedComment = commentRepository.save(newComment);

        return CommentCreateResponse.from(savedComment);
    }

    // 특정 게시글의 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<CommentGetAllResponse> getComment(Long postId) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        if (findPost.isDeleted()) {
            throw new CustomException((ErrorCode.NOT_FOUND_POST));
        }

        List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(postId);

        return comments.stream()
                .map(CommentGetAllResponse::from)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    public CommentUpdateResponse updateComment(Long id, CommentUpdateRequest request, CustomUserDetails userDetails) {

        Comment findComment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        User user = findComment.getUser();
        if (!user.getEmail().equals(userDetails.getUserEmail())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        findComment.update(request.getComment());
        commentRepository.flush();

        return CommentUpdateResponse.from(findComment);
    }

    // 댓글 삭제 (소프트 딜리트)
    public void deleteComment(Long id, CustomUserDetails userDetails) {

        Comment findComment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        User user = findComment.getUser();
        if (!user.getEmail().equals(userDetails.getUserEmail())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        findComment.softDelete();
    }
}
