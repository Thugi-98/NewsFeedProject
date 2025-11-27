package com.example.newsfeedproject.comment.dto.response;

import com.example.newsfeedproject.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentCreateResponse {

    private final Long id;
    private final Long postId;
    private final String commentUserName;
    private final String comment;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CommentCreateResponse from(Comment comment) {
        return new CommentCreateResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getName(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
