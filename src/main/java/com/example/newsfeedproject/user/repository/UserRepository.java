package com.example.newsfeedproject.user.repository;

import com.example.newsfeedproject.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByName(String name);

    // @SQLRestriction을 무시하고, is_deleted 여부와 관계없이 ID로 유저를 찾기
    @Query(value = "SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailWithDeleted(String email);
}
