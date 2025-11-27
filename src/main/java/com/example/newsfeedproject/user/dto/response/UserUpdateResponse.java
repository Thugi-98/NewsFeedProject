package com.example.newsfeedproject.user.dto.response;

import com.example.newsfeedproject.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserUpdateResponse {

    private Long id;
    private String username;
    private String email;
    private LocalDate birth;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserUpdateResponse from(User user) {
        return new UserUpdateResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBirth(),
                user.getIntroduction(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}
