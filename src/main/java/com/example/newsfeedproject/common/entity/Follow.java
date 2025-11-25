package com.example.newsfeedproject.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "follows")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로우를 하는 유저
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // 팔로우를 당한 유저
    @ManyToOne
    @JoinColumn(name = "targetId", nullable = false)
    private User target;

    // 생성자
    public Follow(User user, User target) {
        this.user = user;
        this.target = target;
    }

}