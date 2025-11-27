package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 댓글 Repository 인터페이스
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 삭제되지않은 모든 댓글을 생성일 기준 오름차순으로 조회 (최신순)
    List<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(Long postId);

    // 특정 게시물의 삭제되지 않은 댓글 개수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId AND c.isDeleted = false")
    long countByPostId(@Param("postId") Long postId);
}
