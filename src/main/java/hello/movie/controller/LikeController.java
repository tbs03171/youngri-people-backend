package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.LikeRequestDTO;
import hello.movie.service.LikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@SecurityRequirement(name = "Bearer Authentication")
public class LikeController {
    private final LikeService likeService;
    @PostMapping("/{reviewid}")
    public ResponseEntity<CustomResponse> insert(@PathVariable("reviewid") Long reviewid,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {

        LikeRequestDTO likeRequestDTO = likeService.createLikeRequestDto(principalDetails.getMember().getId(),
                reviewid);

        likeService.insert(likeRequestDTO);
        CustomResponse response = CustomResponse.builder()
                .message("좋아요 성공")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/{reviewid}")
    public ResponseEntity<CustomResponse> delete(@PathVariable("reviewid") Long reviewid,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        LikeRequestDTO likeRequestDTO = likeService.createLikeRequestDto(principalDetails.getMember().getId(),
                reviewid);

        likeService.delete(likeRequestDTO);
        CustomResponse response = CustomResponse.builder()
                .message("좋아요 취소")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status/{reviewid}")
    public ResponseEntity<CustomResponse> checkLikeStatus(@PathVariable("reviewid") Long reviewid,
                                                          @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        LikeRequestDTO likeRequestDTO = likeService.createLikeRequestDto(principalDetails.getMember().getId(),
                reviewid);
        boolean status =likeService.isLike(likeRequestDTO);
        CustomResponse response = CustomResponse.builder()
                .message("좋아요 상태 유무")
                .data(status)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
