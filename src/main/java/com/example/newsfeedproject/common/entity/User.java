package com.example.newsfeedproject.common.entity;

import com.example.newsfeedproject.user.dto.request.UpdateUserRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate birth;
    private String introduction;
    private boolean isDeleted = false;

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