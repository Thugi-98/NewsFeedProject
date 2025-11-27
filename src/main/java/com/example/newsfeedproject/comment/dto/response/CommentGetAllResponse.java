package com.example.newsfeedproject.comment.dto.response;

import com.example.newsfeedproject.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentGetAllResponse {

    private Long id;
    private Long postId;
    private String userName;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentGetAllResponse from(Comment comment) {
        return CommentGetAllResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userName(comment.getUser().getName())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
