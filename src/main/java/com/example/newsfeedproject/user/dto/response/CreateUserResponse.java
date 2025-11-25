package com.example.newsfeedproject.user.dto.response;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
public class CreateUserResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final LocalDate birth;
    private final String introduction;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CreateUserResponse(Long id, String name, String email, LocalDate birth, String introduction, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.introduction = introduction;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
