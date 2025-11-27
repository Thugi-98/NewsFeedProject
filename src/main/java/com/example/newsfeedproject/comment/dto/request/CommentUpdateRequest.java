package com.example.newsfeedproject.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *댓글 수정 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest {

    // 수정할 댓글 내용
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String comment;
}
