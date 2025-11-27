package com.example.newsfeedproject.user.dto.response;

import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadUserResponse {

    private Long id;
    private String name;    // 이름
    private String email;   // 이메일
    private LocalDate birth;    // 생일(YYYY-mm-dd)
    private String introduction;   // 소개
    private LocalDateTime createdAt;     // 유저 생성일
    private LocalDateTime modifiedAt;   // 유저 수정일
    private Boolean followPrivate;  // 계정 비공개 여부

    // UserDto를 받아 반환하는 메서드
    public static ReadUserResponse from (UserDto dto) {
        return new ReadUserResponse(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getBirth(),
                dto.getIntroduction(),
                dto.getCreatedAt(),
                dto.getModifiedAt(),
                dto.getFollowPrivate()
        );
    }

    // User 엔티티를 받아 변환하는 메서드
    public static ReadUserResponse from (User user) {
        return new ReadUserResponse(
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
