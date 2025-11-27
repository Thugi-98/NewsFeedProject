package com.example.newsfeedproject.post.dto.response;

import com.example.newsfeedproject.common.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostGetAllResponse {

    private final Long id;
    private final String userName;
    private final String title;
    private final int commentCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static PostGetAllResponse from(Post post, long commentCount) {
        return new PostGetAllResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                (int) commentCount,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
