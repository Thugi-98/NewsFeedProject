package com.example.newsfeedproject.like.commentLike.repository;

import com.example.newsfeedproject.common.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @Query("SELECT p FROM CommentLike p WHERE p.comment.id = :commentId AND p.user.id = :userId")
    Optional<CommentLike> findByCommentIdAndUserId(
            @Param("commentId") Long commentId,
            @Param("userId") Long userId
    );

    @Query("SELECT COUNT(p) FROM CommentLike p WHERE p.comment.id = :commentId")
    Long countByCommentId(@Param("commentId") Long commentId);
}