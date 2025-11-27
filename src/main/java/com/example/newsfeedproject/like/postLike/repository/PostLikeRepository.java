package com.example.newsfeedproject.like.postLike.repository;

import com.example.newsfeedproject.common.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query("SELECT p FROM PostLike p WHERE p.post.id = :postId AND p.user.id = :userId")
    Optional<PostLike> findByPostIdAndUserId(
            @Param("postId") Long postId,
            @Param("userId") Long userId
    );
}