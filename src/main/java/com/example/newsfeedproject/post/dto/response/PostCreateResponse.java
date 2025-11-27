package com.example.newsfeedproject.post.dto.response;

import com.example.newsfeedproject.common.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostCreateResponse {
    private final Long id;
    private final String postUserName;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static PostCreateResponse from(Post post) {
        return new PostCreateResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
