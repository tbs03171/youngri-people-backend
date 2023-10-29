package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.model.Follow;
import hello.movie.model.Member;
import hello.movie.service.FollowerService;
import hello.movie.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowerService followerService;

    //팔로우 기능
    @PostMapping("/follow")
    public ResponseEntity<CustomResponse> follow(@RequestParam Long followerId, @RequestParam Long followingId){
        followerService.follow(followerId, followingId);

        CustomResponse response = CustomResponse.builder()
                .message("구독 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    //언팔로우 기능
    @PostMapping("/unfollow")
    public ResponseEntity<CustomResponse> unfollow(@RequestParam Long followerId, @RequestParam Long followingId){
        followerService.unfollow(followerId, followingId);

        CustomResponse response = CustomResponse.builder()
                .message("구독 취소")
                .build();
        return ResponseEntity.ok(response);
    }

    //팔로우한 사람 목록 보기
    @GetMapping("/followers/{memberId}")
    public ResponseEntity<CustomResponse> getFollowersList(@PathVariable Long memberId){
        Optional<List<Member>> followersList = followerService.getFollowersList(memberId);

        CustomResponse response = CustomResponse.builder()
                .message("팔로우한 사람 목록 조회 성공")
                .data(followersList)
                .build();
        return ResponseEntity.ok(response);

    }
}
