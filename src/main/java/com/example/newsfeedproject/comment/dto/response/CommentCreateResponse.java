package com.example.newsfeedproject.comment.dto.response;

import com.example.newsfeedproject.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentCreateResponse {

    private Long id;
    private Long postId;
    private String userName;
    private String comment;
    private LocalDateTime createdAt;

    public static CommentCreateResponse from(Comment comment) {
        return CommentCreateResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userName(comment.getUser().getName())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
