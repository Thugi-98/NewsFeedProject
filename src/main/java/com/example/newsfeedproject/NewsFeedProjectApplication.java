package com.example.newsfeedproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NewsFeedProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsFeedProjectApplication.class, args);
    }
}
