package com.example.newsfeedproject.follow.dto;

import lombok.Getter;

@Getter
public class ReadFollowResponse {

    private final Long id;
    private final Long userId;
    private final Long targetId;
    private final String targetName;

    public ReadFollowResponse(Long id, Long userId, Long targetId, String targetName) {
        this.id = id;
        this.userId = userId;
        this.targetId = targetId;
        this.targetName = targetName;
    }
}