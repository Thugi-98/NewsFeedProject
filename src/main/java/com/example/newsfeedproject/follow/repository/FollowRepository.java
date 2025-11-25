package com.example.newsfeedproject.follow.repository;

import com.example.newsfeedproject.common.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

}