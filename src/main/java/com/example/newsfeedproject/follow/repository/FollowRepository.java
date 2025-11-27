package com.example.newsfeedproject.follow.repository;

import com.example.newsfeedproject.common.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f FROM Follow f WHERE f.user.id = :userId AND f.target.id = :targetId")
    Optional<Follow> findByUserIdAndTargetId (
            @Param("userId") Long userId,
            @Param("targetId") Long targetId
    );

    @Query("SELECT f.target.id FROM Follow f WHERE f.user.id = :userId")
    List<Long> findTargetIdByUserId (
            @Param("userId") Long userId
    );
}