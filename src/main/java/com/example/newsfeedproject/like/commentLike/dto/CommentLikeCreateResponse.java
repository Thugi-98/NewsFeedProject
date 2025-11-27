package com.example.newsfeedproject.like.commentLike.dto;

import lombok.Getter;

@Getter
public class CommentLikeCreateResponse {

    private final Long id;
    private final Long commentId;
    private final Long userId;

    public CommentLikeCreateResponse(Long id, Long commentId, Long userId) {
        this.id = id;
        this.commentId = commentId;
        this.userId = userId;
    }
}
