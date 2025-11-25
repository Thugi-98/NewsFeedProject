package com.example.newsfeedproject.follow.service;

import com.example.newsfeedproject.common.dto.ErrorCode;
import com.example.newsfeedproject.common.entity.Follow;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.follow.dto.CreateFollowRequest;
import com.example.newsfeedproject.follow.dto.CreateFollowResponse;
import com.example.newsfeedproject.follow.dto.ReadFollowResponse;
import com.example.newsfeedproject.follow.repository.FollowRepository;
import com.example.newsfeedproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /* CREATE - 다른 유저 팔로우하기 */
    @Transactional
    public @Nullable CreateFollowResponse create(CreateFollowRequest request) {

        /* 1. 예외가 발생되는 경우인지 먼저 확인하기 */
        List<Follow> follows = followRepository.findAll();

        /* 1-1. 내가 나를 팔로우하는 경우 - userId와 TargetId가 동일한지 조회 */
        if (request.getUserId().equals(request.getTargetId())) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        /* 1-2. 이미 팔로우되어 있는 경우 */
        for (Follow follow : follows) {
            if (follow.getUser().getId().equals(request.getUserId()) && follow.getTarget().getId().equals(request.getTargetId())) {
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }
        }

        /* 2. 팔로우 하는 유저가 존재하는지 확인 - user와 target이 존재하는 유저인지 확인 */
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );
        User target = userRepository.findById(request.getTargetId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        /* 3. 검증이 완료될 경우 팔로우하기 */
        Follow follow = new Follow(user, target);
        followRepository.save(follow);

        return new CreateFollowResponse(follow.getId(), follow.getUser().getId(), follow.getTarget().getId(), follow.getTarget().getName());
    }

    /* READ - 특정 유저가 팔로우하는 유저 목록 확인하기 */
    @Transactional
    public @Nullable List<ReadFollowResponse> read(Long userId) {

        /* 1. 전체 팔로우 목록 불러오기 */
        List<Follow> follows = followRepository.findAll();

        /* 2. 특정 유저가 팔로우하는 목록 불러오기 - userId가 동일한 객체만 불러오기 */
        List<ReadFollowResponse> dtos = new ArrayList<>();
        for (Follow follow : follows) {
            if (follow.getUser().getId().equals(userId))
                dtos.add(new ReadFollowResponse(follow.getId(), follow.getUser().getId(), follow.getTarget().getId(), follow.getTarget().getName()));
        }

        return dtos;
    }

    /* DELETE - 특정 ROW 삭제 */
    @Transactional
    public void delete(Long userId, Long targetId) {

        /* 1. 팔로우 목록 중 userId와 targetId가 동일한 객체 찾기 */
        List<Follow> follows = followRepository.findAll();

        /* 2. 해당 객체를 찾을 경우 바로 ★-=삭★제=-★ */
        for (Follow follow : follows) {
            if (follow.getUser().getId().equals(userId) && follow.getTarget().getId().equals(targetId)) {
                followRepository.deleteById(follow.getId());
            }
        }

    }
}