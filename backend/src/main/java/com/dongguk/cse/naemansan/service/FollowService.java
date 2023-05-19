package com.dongguk.cse.naemansan.service;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.domain.Follow;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.dto.response.FollowDto;
import com.dongguk.cse.naemansan.repository.FollowRepository;
import com.dongguk.cse.naemansan.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    public Boolean createFollow(Long followingId, Long followerId) {
        // 유저 존재, 이미 팔로잉 되어있는지 유무 확인
        User followingUser = userRepository.findById(followingId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        User followerUser = userRepository.findById(followerId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        followRepository.findByFollowingUserAndFollowerUser(followingUser, followerUser).ifPresent(follow -> { throw new RestApiException(ErrorCode.EXIST_ENTITY_REQUEST); });

        followRepository.save(Follow.builder()
                .followingUser(followingUser)
                .followerUser(followerUser).build());

        return Boolean.TRUE;
    }

    public List<FollowDto> readFollowing(Long followingId) {
        // User 존재 유무 확인
        User followingUser = userRepository.findById(followingId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        
        // Dto 변환
        List<FollowDto> followDtoList = new ArrayList<>();
        for (Follow follow : followingUser.getFollowings()) {
            followDtoList.add(FollowDto.builder()
                    .userId(follow.getFollowerUser().getId())
                    .userName(follow.getFollowerUser().getName()).build());
        }

        // Dto 반환
        return followDtoList;
    }

    public List<FollowDto> readFollower(Long followerId) {
        // User 존재 유무 확인
        User followerUser = userRepository.findById(followerId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        // Dto 변환
        List<FollowDto> followDtoList = new ArrayList<>();
        for (Follow follow : followerUser.getFollowers()) {
            followDtoList.add(FollowDto.builder()
                    .userId(follow.getFollowingUser().getId())
                    .userName(follow.getFollowingUser().getName()).build());
        }

        // Dto 반환
        return followDtoList;
    }

    public Boolean deleteFollow(Long followingId, Long followerId) {
        // 유저 존재, 팔로잉 중 되어있는지 유무 확인
        User followingUser = userRepository.findById(followingId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        User followerUser = userRepository.findById(followerId).orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));
        Follow follow = followRepository.findByFollowingUserAndFollowerUser(followingUser, followerUser).orElseThrow(() -> new RestApiException(ErrorCode.NOT_EXIST_ENTITY_REQUEST));

        // 팔로우 관계 삭제
        followRepository.delete(follow);

        return Boolean.TRUE;
    }
}
