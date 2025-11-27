package com.example.newsfeedproject.comment.dto.response;

import com.example.newsfeedproject.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentGetAllResponse {

    private final Long id;
    private final Long postId;
    private final String CommentUserName;
    private final String comment;
    private final Long commentLikeCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CommentGetAllResponse from(Comment comment, Long commentLikeCount) {
        return new CommentGetAllResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getName(),
                comment.getComment(),
                commentLikeCount,
                comment.getCreatedAt(),
                comment.getModifiedAt()
                );
    }
}
