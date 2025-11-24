package com.example.newsfeedproject.post.repository;

import com.example.newsfeedproject.common.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {


}
