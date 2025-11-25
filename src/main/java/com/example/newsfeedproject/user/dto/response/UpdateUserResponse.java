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
public class UpdateUserResponse {

    private Long id;
    private String username;
    private String email;
    private LocalDate birth;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UpdateUserResponse from (UserDto dto) {
        return new UpdateUserResponse(
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
