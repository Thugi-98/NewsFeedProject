package com.example.newsfeedproject.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate birth;
    private String introduction;

    public User(String name, String email, String password, LocalDate birth, String introduction) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.introduction = introduction;
    }

}