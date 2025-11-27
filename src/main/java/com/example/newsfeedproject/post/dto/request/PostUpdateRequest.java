package com.example.newsfeedproject.post.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateRequest {

    @Size(min = 1, max = 100)
    private String title;
    @Size(min = 1, max = 200)
    private String content;
}
