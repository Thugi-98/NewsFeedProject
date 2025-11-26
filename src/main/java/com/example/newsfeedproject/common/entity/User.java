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

    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate birth;
    private String introduction;
    private boolean isDeleted = false;
    private Boolean followPrivate = false;

    // 생성자
    public User(String name, String email, String password, LocalDate birth, String introduction) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.introduction = introduction;
    }

    // 속성
    public void update(UpdateUserRequest request, String newEncodedPassword) {
        // 일반 정보 업데이트
        if(request.getName() != null && !request.getName().trim().isEmpty()) {
            this.name = request.getName().trim();
        }
        if(request.getBirth() != null) {
            this.birth = request.getBirth();
        }
        if(request.getIntroduction() != null && !request.getIntroduction().trim().isEmpty()) {
            this.introduction = request.getIntroduction().trim();
        }
        if(request.getFollowPrivate() != null) {
            this.followPrivate = request.getFollowPrivate();
        }

        // 비밀번호 수정값이 있는 경우에만 비밀번호를 업데이트
        if (newEncodedPassword != null && !newEncodedPassword.isEmpty()) {
            this.password = newEncodedPassword;
        }
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    // 이미 삭제된 유저인지 확인하는 메서드 추가 (예외 처리를 위해 사용)
//    public boolean isDeleted() {
//        return this.isDeleted;
//    }
}