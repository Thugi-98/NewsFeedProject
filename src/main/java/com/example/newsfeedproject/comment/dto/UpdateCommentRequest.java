package com.example.newsfeedproject.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UpdateCommentRequest {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String comment;
}
