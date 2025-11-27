package com.example.newsfeedproject.comment.dto.request;

import com.example.newsfeedproject.common.entity.Comment;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 댓글 생성 요청 DTO
 */
@Getter
@AllArgsConstructor
@Builder
public class CommentCreateRequest {

    // 댓글 내용
    @NotBlank(message = "댓글 내용을 입력해주세요")
    @Size(min = 1, max = 100, message = "댓글은 100자를 초과할 수 없습니다.")
    private String comment;

    // DTO를 Comment 엔티티로 변환
    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .comment(comment)
                .user(user)
                .post(post)
                .build();
    }
}
