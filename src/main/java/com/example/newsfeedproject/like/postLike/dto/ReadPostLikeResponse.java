package com.example.newsfeedproject.like.postLike.dto;

import lombok.Getter;

@Getter
public class ReadPostLikeResponse {

    private final Long id;
    private final Long postId;
    private final Long userId;

    public ReadPostLikeResponse(Long id, Long postId, Long userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }
}
