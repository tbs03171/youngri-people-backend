package hello.movie.controller;

import hello.movie.model.Follow;
import hello.movie.model.Member;
import hello.movie.service.FollowerService;
import hello.movie.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.messageinterpolation.FormatterWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowerService followerService;
    private final MemberService memberService;

    //팔로우 기능
    @PostMapping("/follow")
    public ResponseEntity<String> follow(@RequestParam Long followerId, @RequestParam Long followingId){

        Member follower = memberService.findById(followerId);
        Member following = memberService.findById(followingId);

        Follow follow = new Follow(follower, following);

        followerService.follow(follow);
        return ResponseEntity.ok("구독 성공");
    }

    //언팔로우 기능
    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestParam Long followerId, @RequestParam Long followingId){

        Member follower = memberService.findById(followerId);
        Member followee = memberService.findById(followingId);

        Follow follow = followerService.findByFollowerAndFollowee(follower, followee);
        followerService.unfollow(follow);

        return ResponseEntity.ok("구독 취소");
    }

    //팔로우한 사람 목록 보기
    @GetMapping("/followers/{memberId}")
    public ResponseEntity<List<Member>> getFollowersList(@PathVariable Long memberId){
        List<Member> followersList = followerService.getFollowersList(memberId);
        return ResponseEntity.ok(followersList);
    }
}
