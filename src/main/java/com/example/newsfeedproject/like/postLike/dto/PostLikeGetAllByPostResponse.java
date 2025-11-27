package com.example.newsfeedproject.like.postLike.dto;

import lombok.Getter;

@Getter
public class PostLikeGetAllByPostResponse {

    private final Long id;
    private final Long postId;
    private final Long postLikeUserId;

    public PostLikeGetAllByPostResponse(Long id, Long postId, Long postLikeUserId) {
        this.id = id;
        this.postId = postId;
        this.postLikeUserId = postLikeUserId;
    }
}
