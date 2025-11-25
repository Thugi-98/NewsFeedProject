package com.example.newsfeedproject.post.dto;

import lombok.Getter;

@Getter
public class UpdatePostRequest {

    private Long userId;
    private String title;
    private String content;
}
