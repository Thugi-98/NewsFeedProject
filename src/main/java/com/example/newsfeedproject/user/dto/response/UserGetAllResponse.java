package com.example.newsfeedproject.user.dto.response;

import com.example.newsfeedproject.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserGetAllResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDate birth;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean followPrivate;

    public static UserGetAllResponse from(User user) {
        return new UserGetAllResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBirth(),
                user.getIntroduction(),
                user.getCreatedAt(),
                user.getModifiedAt(),
                user.getFollowPrivate()
        );
    }
}
