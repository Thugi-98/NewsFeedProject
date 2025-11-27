package com.example.newsfeedproject.post.dto.response;

import com.example.newsfeedproject.comment.dto.response.CommentGetAllResponse;
import com.example.newsfeedproject.common.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class PostGetOneResponse {
    private final Long id;
    private final String userName;
    private final String title;
    private final String content;
    private final List<CommentGetAllResponse> comments;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static PostGetOneResponse from(Post post, List<CommentGetAllResponse> comments) {

        return new PostGetOneResponse(
                post.getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getContent(),
                comments,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
