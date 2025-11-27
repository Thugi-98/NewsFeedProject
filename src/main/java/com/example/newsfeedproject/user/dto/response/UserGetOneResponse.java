package com.example.newsfeedproject.user.dto.response;

import com.example.newsfeedproject.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserGetOneResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDate birth;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean followPrivate;

    public static UserGetOneResponse from(User user) {
        return new UserGetOneResponse(
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
