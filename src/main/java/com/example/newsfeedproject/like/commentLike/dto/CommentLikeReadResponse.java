package com.example.newsfeedproject.like.commentLike.dto;

import lombok.Getter;

@Getter
public class CommentLikeReadResponse {

    private final Long id;
    private final Long commentId;
    private final Long commentLikeUserId;

    public CommentLikeReadResponse(Long id, Long commentId, Long commentLikeUserId) {
        this.id = id;
        this.commentId = commentId;
        this.commentLikeUserId = commentLikeUserId;
    }
}
