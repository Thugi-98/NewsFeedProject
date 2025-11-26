package com.example.newsfeedproject.user.dto;

import com.example.newsfeedproject.common.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;    // 이름
    private String email;   // 이메일
    private LocalDate birth;    // 생일(YYYY-mm-dd)
    private String introduction;   // 소개
    private LocalDateTime createdAt;     // 유저 생성일
    private LocalDateTime modifiedAt;   // 유저 수정일
    private Boolean followPrivate;

    public static UserDto from (User user) {
        return new UserDto(
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
