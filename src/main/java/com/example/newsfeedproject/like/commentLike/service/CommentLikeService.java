package com.example.newsfeedproject.like.commentLike.service;

import com.example.newsfeedproject.common.entity.Comment;
import com.example.newsfeedproject.common.entity.CommentLike;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.like.commentLike.dto.CommentLikeCreateResponse;
import com.example.newsfeedproject.like.commentLike.dto.CommentLikeGetAllByCommentResponse;
import com.example.newsfeedproject.like.commentLike.repository.CommentLikeRepository;
import com.example.newsfeedproject.comment.repository.CommentRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentLikeCreateResponse create(CustomUserDetails userDetails, Long commentId) {

        // 1. 접근 유저가 누구인지 확인
        User user = userRepository.findByEmailAndIsDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_DENIED));

        // 2. 좋아요가 달릴 댓글 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
        if (comment.isDeleted()) {
            throw new CustomException((ErrorCode.NOT_FOUND_COMMENT));
        }

        // 3. 예외가 발생되는 경우인지 확인하기 위해 전체 commentLike 목록 불러오기
        List<CommentLike> commentLikes = commentLikeRepository.findAll();

        // 3-1. 이미 좋아요를 누른 경우 - comment Id와 user Id가 이미 DB에 함께 존재하는 경우
        for (CommentLike commentLike : commentLikes) {
            if (commentLike.getComment().getId().equals(comment.getId()) && commentLike.getUser().getId().equals(user.getId())) {
                throw new CustomException(ErrorCode.ALREADY_COMMENTLIKE);
            }
        }

        // 4. 좋아요를 누른 적이 없을 경우 좋아요 생성 후 반환
        CommentLike commentLike = new CommentLike(comment, user);
        commentLikeRepository.save(commentLike);

        return new CommentLikeCreateResponse(commentLike.getId(), commentLike.getComment().getId(), commentLike.getUser().getId());

    }

    @Transactional
    public List<CommentLikeGetAllByCommentResponse> read(Long commentId) {

        // 1. commentId로 포스트 찾기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
        if (comment.isDeleted()) {
            throw new CustomException((ErrorCode.NOT_FOUND_COMMENT));
        }

        // 2. 해당 Comment의 좋아요 전부 출력하기
        List<CommentLike> commentLikes = commentLikeRepository.findAll();

        List<CommentLikeGetAllByCommentResponse> dtos = new ArrayList<>();

        for (CommentLike commentLike : commentLikes) {
            if (commentLike.getComment().getId().equals(comment.getId())) {
                dtos.add(new CommentLikeGetAllByCommentResponse(commentLike.getId(), commentLike.getComment().getId(), commentLike.getUser().getId()));
            }
        }

        return dtos;

    }

    @Transactional
    public void delete(Long commentId, CustomUserDetails userDetails) {

        // 1. 접근 유저가 누구인지 확인
        User user = userRepository.findByEmailAndIsDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_DENIED));

        // 2. 좋아요를 취소할 댓글 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));
        if (comment.isDeleted()) {
            throw new CustomException((ErrorCode.NOT_FOUND_COMMENT));
        }

        // 3. 조건에 해당하는 CommentLike 찾기
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(comment.getId(), user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENTLIKE_NOT_FOUND));

        commentLikeRepository.deleteById(commentLike.getId());
    }
}
