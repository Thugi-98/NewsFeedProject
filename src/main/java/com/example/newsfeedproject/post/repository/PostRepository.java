package com.example.newsfeedproject.post.repository;

import com.example.newsfeedproject.common.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {


    Page<Post> findByUserIdAndIsDeleteFalse(Long userId, Pageable pageable);

    Page<Post> findByIsDeleteFalse(Pageable pageable);

    Optional<Post> findByIdAndIsDeleteFalse(Long id);
}
