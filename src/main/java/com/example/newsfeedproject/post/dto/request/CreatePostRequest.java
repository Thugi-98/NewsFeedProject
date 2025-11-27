package com.example.newsfeedproject.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreatePostRequest {

    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(min = 1, max = 100)
    private String title;
    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(min = 1, max = 200)
    private String content;
}
