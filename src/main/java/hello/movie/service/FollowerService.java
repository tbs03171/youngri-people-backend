package hello.movie.service;


import hello.movie.model.Follow;
import hello.movie.model.Member;
import hello.movie.repository.FollowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final MemberService memberService;

    @Transactional
    public void follow(Long followerId, Long followingId){
        Optional<Member> follower = memberService.findById(followerId);
        Optional<Member> following = memberService.findById(followingId);

        Follow follow = createFollow(follower, following);
        followerRepository.save(follow);
    }

    private static Follow createFollow(Optional<Member> follower, Optional<Member> following) {
        Follow follow = new Follow(follower.get(), following.get());
        return follow;
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId){
        Optional<Member> follower = memberService.findById(followerId);
        Optional<Member> followee = memberService.findById(followingId);

        Follow follow = findByFollowerAndFollowee(follower.get(), followee.get());
        followerRepository.delete(follow);
    }

    public Follow findByFollowerAndFollowee(Member follower, Member followee){
        return followerRepository.findByFollowerAndFollowee(follower, followee);
    }

    public Optional<List<Member>> getFollowersList(Long id) {
        return followerRepository.findAllByFollower(id);
    }
}
