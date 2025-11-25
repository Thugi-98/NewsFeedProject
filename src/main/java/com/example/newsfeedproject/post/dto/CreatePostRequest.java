package com.example.newsfeedproject.post.dto;

import lombok.Getter;

@Getter
public class CreatePostRequest {

    private Long userId;
    private String title;
    private String content;
}
