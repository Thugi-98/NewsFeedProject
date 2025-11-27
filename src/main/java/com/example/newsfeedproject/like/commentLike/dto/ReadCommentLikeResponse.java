package com.example.newsfeedproject.like.commentLike.dto;

import lombok.Getter;

@Getter
public class ReadCommentLikeResponse {

    private final Long id;
    private final Long commentId;
    private final Long userId;

    public ReadCommentLikeResponse(Long id, Long commentId, Long userId) {
        this.id = id;
        this.commentId = commentId;
        this.userId = userId;
    }
}
