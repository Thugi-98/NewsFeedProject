package com.example.newsfeedproject.common.entity;

import com.example.newsfeedproject.user.dto.request.UpdateUserRequest;
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
    private Long id;    // 사용자id

    @Column(length = 30, nullable = false)
    private String name;    // 이름

    @Column(length = 50, nullable = false, unique = true)
    private String email;   // 이메일

    @Column(length = 255, nullable = false)
    private String password;    // 비밀번호

    private LocalDate birth;    // 생일(YYYY-mm-dd)

    @Column(length = 100)
    private String introduction;   // 소개

    public User(String name, String email, String password, LocalDate birth, String introduction) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.introduction = introduction;
    }

    public void update(UpdateUserRequest request) {
         this.name = request.getName() != null ? request.getName() : this.name;
         this.password = request.getPassword() != null ? request.getPassword() : this.password;
         this.birth = request.getBirth() != null ? request.getBirth() : this.birth;
         this.introduction = request.getIntroduction() != null ? request.getIntroduction() : this.introduction;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}