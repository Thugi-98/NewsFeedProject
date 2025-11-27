package com.example.newsfeedproject.comment.dto.response;

import com.example.newsfeedproject.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentGetAllResponse {

    private Long id;
    private Long postId;
    private String CommentUserName;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentGetAllResponse from(Comment comment) {
        return new CommentGetAllResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getName(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
                );
    }
}
