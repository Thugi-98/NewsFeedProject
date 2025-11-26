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

    public void update(UpdateUserRequest request, String newEncodedPassword) {
        // 일반 정보 업데이트
        this.name = request.getName();
        this.birth = request.getBirth();
        this.introduction = request.getIntroduction();

        // 비밀번호 수정값이 있는 경우에만 비밀번호를 업데이트
        if (newEncodedPassword != null) {
            this.password = newEncodedPassword;
        }
    }
}