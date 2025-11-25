package com.example.newsfeedproject;

import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 유저 없으면 생성
        if (userRepository.count() == 0) {
            User u1 = new User("user1", "user1@test.com", "1234", LocalDate.of(2001,6,11), "ㅎㅇ");
            User u2 = new User("user2", "user2@test.com", "12345", LocalDate.of(2000,5,12), "해윙");
            User u3 = new User("user3", "user3@test.com", "123456", LocalDate.of(2001,2,8), "할로");

            userRepository.save(u1);
            userRepository.save(u2);
            userRepository.save(u3);
        }
    }
}