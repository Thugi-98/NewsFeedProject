package com.example.newsfeedproject.comment.repository;

import com.example.newsfeedproject.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 모든 댓글 조회
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postid);
}
