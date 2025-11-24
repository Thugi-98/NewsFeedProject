package com.example.newsfeedproject.user.dto.response;

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
    private String introduce;   // 소개
    private LocalDateTime createdAt;     // 유저 생성일
    private LocalDateTime modifiedAt;   // 유저 수정일

    public static ReadUserResponse from (UserDto dto) {
        return new ReadUserResponse(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getBirth(),
                dto.getIntroduction(),
                dto.getCreatedAt(),
                dto.getModifiedAt()
        );
    }
}
