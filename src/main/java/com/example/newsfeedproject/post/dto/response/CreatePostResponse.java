package com.example.newsfeedproject.post.dto.response;

import com.example.newsfeedproject.common.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreatePostResponse {
    private final Long id;
    private final String userName;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CreatePostResponse from(Post post) {
        return new CreatePostResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
