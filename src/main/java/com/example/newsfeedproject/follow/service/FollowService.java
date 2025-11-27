package com.example.newsfeedproject.follow.service;

import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.entity.Follow;
import com.example.newsfeedproject.common.entity.User;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.security.user.CustomUserDetails;
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
    public @Nullable CreateFollowResponse create(CustomUserDetails userDetails, Long targetId) {

        /* 1. 접근 유저가 누구인지 확인 */
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCESS_DENIED)
        );

        /* 2. 팔로우 하는 유저가 존재하는지 확인 - target이 존재하는 유저인지 확인 */
        User target = userRepository.findById(targetId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        /* 3. 예외가 발생되는 경우인지 확인을 위해 follow 전체 목록 불러오기 */
        List<Follow> follows = followRepository.findAll();

        /* 3-1. 내가 나를 팔로우하는 경우 - user.email과 target.email이 동일한지 검증 */
        if (user.getId().equals(target.getId())) {
            throw new CustomException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        /* 3-2. 이미 팔로우되어 있는 경우 */
        for (Follow follow : follows) {
            if (follow.getUser().getId().equals(user.getId()) && follow.getTarget().getId().equals(target.getId())) {
                throw new CustomException(ErrorCode.ALREADY_FOLLOWING);
            }
        }

        /* 4. 검증이 완료될 경우 팔로우하기 */
        Follow follow = new Follow(user, target);
        followRepository.save(follow);

        return new CreateFollowResponse(follow.getId(), follow.getUser().getId(), follow.getTarget().getId(), follow.getTarget().getName(), follow.getTarget().getEmail());
    }

    /* READ - 특정 유저가 팔로우하는 유저 목록 확인하기 */
    @Transactional
    public @Nullable List<ReadFollowResponse> read(CustomUserDetails userDetails, Long targetId) {

        /* 1. 접근 유저가 누구인지 확인 */
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCESS_DENIED)
        );

        /* 2. 전체 팔로우 목록 불러오기 */
        List<Follow> follows = followRepository.findAll();

        /* 3-1. target이 본인인 경우, 혹은 targetId가 없는 경우 - 본인의 팔로우 목록 반환 */
        if (targetId == null || targetId.equals(user.getId())) {

            List<ReadFollowResponse> dtos = new ArrayList<>();
            for (Follow follow : follows) {
                if (follow.getUser().getId().equals(user.getId()))
                    dtos.add(new ReadFollowResponse(follow.getId(), follow.getUser().getId(), follow.getTarget().getId(), follow.getTarget().getName(), follow.getTarget().getEmail()));
            }

            return dtos;
        }

        /* 3-2. target이 본인이 아닌 경우 - target의 팔로우 목록 반환 */

        /* 3-2-1. target 존재 여부 확인 */
        User target = userRepository.findById(targetId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        /* 3-2-2. target 비공개 여부 확인 */
        if (target.getFollowPrivate.equals(true)) {
            throw new CustomException(ErrorCode.FOLLOW_LIST_PRIVATE);
        }

        /* 3-2-3. target 팔로우 목록 공개 */
        List<ReadFollowResponse> dtos = new ArrayList<>();
        for (Follow follow : follows) {
            if (follow.getUser().getId().equals(target.getId()))
                dtos.add(new ReadFollowResponse(follow.getId(), follow.getUser().getId(), follow.getTarget().getId(), follow.getTarget().getName(), follow.getTarget().getEmail()));
        }

        return dtos;
    }

    /* DELETE - 특정 ROW 삭제 */
    @Transactional
    public void delete(CustomUserDetails userDetails, Long targetId) {

        /* 1. 접근 유저가 누구인지 확인 */
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCESS_DENIED)
        );

        /* 2. target 유저가 존재하는지 확인 */
        User target = userRepository.findById(targetId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        /* 3. 조건에 해당하는 Follow 찾기 */
        Follow follow = followRepository.findByUserIdAndTargetId(user.getId(), target.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND)
        );

        followRepository.deleteById(follow.getId());

    }
}