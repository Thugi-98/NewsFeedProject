package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 모든 댓글 조회
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postid);

    // 댓글 개수
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId AND c.isDeleted = false")
    long countByPostId(@Param("postId") Long postId);

    // 댓글 목록 (필요할 때만)
    List<Comment> findByPostIdAndIsDeletedFalse(Long postId);
}
