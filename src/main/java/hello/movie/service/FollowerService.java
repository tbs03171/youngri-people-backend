package hello.movie.service;


import hello.movie.model.Follow;
import hello.movie.model.Member;
import hello.movie.repository.FollowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowerService {

    private final FollowerRepository followerRepository;

    @Transactional
    public void follow(Follow follow){
        followerRepository.save(follow);
    }

    @Transactional
    public void unfollow(Follow follow){
        followerRepository.delete(follow);
    }

    public Follow findByFollowerAndFollowee(Member follower, Member followee){
        return followerRepository.findByFollowerAndFollowee(follower, followee);
    }

    public List<Member> getFollowersList(Long memberId) {
        return followerRepository.findAllByFollower(memberId);
    }
}
