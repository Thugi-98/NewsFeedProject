package com.example.newsfeedproject.follow.dto;

import lombok.Getter;

@Getter
public class CreateFollowRequest {

    private Long userId;
    private Long targetId;
}
