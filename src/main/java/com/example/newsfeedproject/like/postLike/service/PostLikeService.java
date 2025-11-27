package com.example.newsfeedproject.like.postLike.service;

import com.example.newsfeedproject.common.entity.Post;
import com.example.newsfeedproject.common.entity.PostLike;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
import com.example.newsfeedproject.like.postLike.dto.CreatePostLikeResponse;
import com.example.newsfeedproject.like.postLike.dto.ReadPostLikeResponse;
import com.example.newsfeedproject.like.postLike.repository.PostLikeRepository;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreatePostLikeResponse create(CustomUserDetails userDetails, Long postId) {

        /* 1. 접근 유저가 누구인지 확인 */
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCESS_DENIED)
        );

        /* 2. 좋아요가 달릴 게시물 확인 */
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        /* 3. 예외가 발생되는 경우인지 확인하기 위해 전체 postLike 목록 불러오기 */
        List<PostLike> postLikes = postLikeRepository.findAll();

        /* 3-1. 이미 좋아요를 누른 경우 - post Id와 user Id가 이미 DB에 함께 존재하는 경우 */
        for (PostLike postLike : postLikes) {
            if (postLike.getPost().getId().equals(post.getId()) && postLike.getUser().getId().equals(user.getId())) {
                throw new CustomException(ErrorCode.ALREADY_POSTLIKE);
            }
        }

        /* 4. 좋아요를 누른 적이 없을 경우 좋아요 생성 후 반환 */
        PostLike postLike = new PostLike(post, user);
        postLikeRepository.save(postLike);

        return new CreatePostLikeResponse(postLike.getId(), postLike.getPost().getId(), postLike.getUser().getId());

    }

    @Transactional
    public List<ReadPostLikeResponse> read(Long postId) {

        /* 1. postId로 포스트 찾기 */
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        /* 2. 해당 Post의 좋아요 전부 출력하기 */
        List<PostLike> postLikes = postLikeRepository.findAll();

        List<ReadPostLikeResponse> dtos = new ArrayList<>();

        for (PostLike postLike : postLikes) {
            if (postLike.getPost().getId().equals(post.getId())) {
                dtos.add(new ReadPostLikeResponse(postLike.getId(), postLike.getPost().getId(), postLike.getUser().getId()));
            }
        }

        return dtos;

    }

    @Transactional
    public void delete(Long postId, CustomUserDetails userDetails) {

        /* 1. 접근 유저가 누구인지 확인 */
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCESS_DENIED)
        );

        /* 2. 좋아요를 취소할 게시물 확인 */
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );

        /* 3. 조건에 해당하는 PostLike 찾기 */
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(post.getId(), user.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.POSTLIKE_NOT_FOUND)
        );

        postLikeRepository.deleteById(postLike.getId());
    }
}
