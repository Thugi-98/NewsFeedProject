package com.example.newsfeedproject.comment.dto.request;

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

    private Long userId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String comment;
}
