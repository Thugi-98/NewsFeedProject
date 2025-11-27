package com.example.newsfeedproject.post.repository;

import com.example.newsfeedproject.common.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);

    Page<Post> findByUserIdInAndIsDeletedFalse(List<Long> userId, Pageable pageable);

    Page<Post> findByIsDeletedFalse(Pageable pageable);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}
