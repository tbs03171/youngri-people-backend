package hello.movie.service;


import hello.movie.dto.FollowResponseDto;
import hello.movie.model.Follow;
import hello.movie.model.Member;
import hello.movie.repository.FollowerRepository;
import hello.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void follow(Long followerId, Long followingId){
        Optional<Member> follower = memberRepository.findById(followerId);
        Optional<Member> following = memberRepository.findById(followingId);

        Follow follow = Follow.createFollow(follower.get(), following.get());
        followerRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId){
        followerRepository.deleteByFollowerIdAndFolloweeId(followerId, followingId);
    }

    public Boolean isFollowing(Long followerId, Long followingId){
        return followerRepository.existsByFollowerIdAndFolloweeId(followerId, followingId);
    }

    public List<FollowResponseDto> getFollowersList(Long memberId) {
        List<Follow> followList = followerRepository.findAllByFollowerId(memberId);

        List<FollowResponseDto> followResponseDtoList = new ArrayList<>();
        for (Follow follow : followList) {
            followResponseDtoList.add(createFollowResponseDto(follow.getFollowee()));
        }
        return followResponseDtoList;
    }

    public FollowResponseDto getFollower(Long followerId, Long followingId) {
        Follow follow = followerRepository.findByFollowerIdAndFolloweeId(followerId, followingId);
        return createFollowResponseDto(follow.getFollowee());
    }

    public FollowResponseDto createFollowResponseDto(Member member){
        return FollowResponseDto.builder()
                .id(member.getId())
                .profilePath(member.getProfilePath())
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .build();
    }
}
