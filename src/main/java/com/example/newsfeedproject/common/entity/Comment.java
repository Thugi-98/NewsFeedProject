package com.example.newsfeedproject.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseEntity{

    // 고유 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시물 id (연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 유저 id (연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 댓글 내용
    @Column(nullable = false,  length = 100)
    private String comment;

    // 삭제 여부 플래그
    private boolean isDeleted = false;

    // 생성자
    @Builder
    public Comment(Post post, User user, String comment) {
        this.post = post;
        this.user = user;
        this.comment = comment;
    }

    // 댓글 수정 메서드
    public void update(String comment) {
            this.comment = comment;
    }

    // 댓글 삭제 메서드
    public void softDelete() {
        this.isDeleted = true;
    }

}