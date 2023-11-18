package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.FollowResponseDto;
import hello.movie.service.FollowerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowerService followerService;

    //팔로우 기능
    @PostMapping("/{followingId}")
    public ResponseEntity<CustomResponse> follow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long followingId){
        Long followerId = principalDetails.getMember().getId();

        followerService.follow(followerId, followingId);

        CustomResponse response = CustomResponse.builder()
                .message("구독 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    //언팔로우 기능
    @DeleteMapping("/{followingId}")
    public ResponseEntity<CustomResponse> unfollow(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long followingId){
        Long followerId = principalDetails.getMember().getId();

        followerService.unfollow(followerId, followingId);

        CustomResponse response = CustomResponse.builder()
                .message("구독 취소")
                .build();
        return ResponseEntity.ok(response);
    }

    //팔로우 상태 확인
    @GetMapping("/status/{followingId}")
    public ResponseEntity<CustomResponse> checkFollowStatus(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long followingId){
        Long followerId = principalDetails.getMember().getId();

        Boolean status = followerService.isFollowing(followerId, followingId);

        CustomResponse response = CustomResponse.builder()
                .message("구독 상태 유무")
                .data(status)
                .build();

        return ResponseEntity.ok(response);
    }

    //팔로우한 사람 목록 보기
    @GetMapping("/followers")
    public ResponseEntity<CustomResponse> getFollowersList(@AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memberId = principalDetails.getMember().getId();

        List<FollowResponseDto> followersList = followerService.getFollowersList(memberId);

        CustomResponse response = CustomResponse.builder()
                .message("팔로우한 사람 목록 조회 성공")
                .data(followersList)
                .build();
        return ResponseEntity.ok(response);

    }
}
