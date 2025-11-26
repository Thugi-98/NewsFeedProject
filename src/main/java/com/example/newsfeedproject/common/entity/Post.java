package com.example.newsfeedproject.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    //속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private boolean isDelete = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //생성자
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    /**
     * 업데이트 기능
     * @param title
     * @param content
     */
    public void update(String title, String content) {
        if(title != null && !title.trim().isEmpty()) {
            this.title = title.trim();
        }
        if(content != null && !content.trim().isEmpty()) {
        this.content = content.trim();
        }
    }

    /**
     * 삭제기능
     */
    public void softDelete() {
        this.isDelete = true;
    }
}