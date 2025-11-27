package com.example.newsfeedproject.comment.dto.response;

import com.example.newsfeedproject.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentUpdateResponse {

    private Long id;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentUpdateResponse from(Comment comment) {
        return new CommentUpdateResponse(
                comment.getId(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
