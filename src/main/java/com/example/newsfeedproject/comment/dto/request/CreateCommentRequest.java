package com.example.newsfeedproject.comment.dto.request;

import com.example.newsfeedproject.common.entity.Comment;
import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequest {

    @NotNull(message = "게시믈 ID는 필수입니다.")
    private Long postId;

    @NotBlank(message = "댓글 내용을 입력해주세요")
    @Size(min = 1, max = 100, message = "댓글은 100자를 초과할 수 없습니다.")
    private String comment;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .comment(comment)
                .user(user)
                .post(post)
                .build();
    }
}
