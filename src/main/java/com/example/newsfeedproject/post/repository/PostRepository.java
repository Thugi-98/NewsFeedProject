package com.example.newsfeedproject.post.repository;

import com.example.newsfeedproject.common.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    //유저 아이디에 해당되는 포스트를 찾기 위한 메서드(페이징)
    Page<Post> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);

    //모든 유저의 포스트를 찾기 위한 메서드(페이징)
    Page<Post> findByIsDeletedFalse(Pageable pageable);

    //한 개의 게시물을 찾기위한 메서드(단건 조회)
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}
