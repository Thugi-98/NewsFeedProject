package com.example.newsfeedproject.post.dto.response;

import com.example.newsfeedproject.common.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostGetAllResponse {

    private final Long id;
    private final String postUserName;
    private final String title;
    private final Long postLikeCount;
    private final Long commentCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static PostGetAllResponse from(Post post, Long postLikeCount, Long commentCount) {
        return new PostGetAllResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                postLikeCount,
                commentCount,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
