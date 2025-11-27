package com.example.newsfeedproject.follow.dto;

import lombok.Getter;

@Getter
public class FollowCreateResponse {

    private final Long id;
    private final Long userId;
    private final Long targetId;
    private final String targetName;
    private final String email;

    public FollowCreateResponse(Long id, Long userId, Long targetId, String targetName, String email) {
        this.id = id;
        this.userId = userId;
        this.targetId = targetId;
        this.targetName = targetName;
        this.email = email;

    }
}