package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.dto.ReviewDTO;
import hello.movie.model.Review;
import hello.movie.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@SecurityRequirement(name = "Bearer Authentication")
public class ReviewController {

    private final ReviewService reviewService;

   /* @GetMapping("/movieall/{movieid}")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("movieid") Long movieid){
        List<ReviewDTO> listOFMovie = reviewService.getListOFMovie(movieid);

        return new ResponseEntity<>(listOFMovie,HttpStatus.OK);
    }*/

    @GetMapping("/memberall/{memberid}")
    public ResponseEntity<CustomResponse> getMemberList(@PathVariable("memberid") Long memberid){
        List<ReviewDTO> listOFMovie = reviewService.getListOFMember(memberid);
        CustomResponse response = CustomResponse.builder()
                .message("작성한 글 조회 성공")
                .data(listOFMovie)
                .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> addReview(@RequestBody ReviewDTO reviewDTO){

        Long findReview = reviewService.register(reviewDTO);
        CustomResponse response = CustomResponse.builder()
                .message("리뷰 작성 성공")
                .data(findReview)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/{reviewid}")
    public ResponseEntity<CustomResponse> modifyReview(@PathVariable Long reviewid, @RequestBody ReviewDTO reviewDTO){

        Review review = reviewService.findOne(reviewid);


        if(!review.getMember().getId().equals(reviewDTO.getMemberid())) {
            CustomResponse response = CustomResponse.builder()
                    .message("글 작성자가 아니라 수정 불가")
                    .data(reviewid)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        reviewService.modify(reviewDTO,reviewid);
        CustomResponse response = CustomResponse.builder()
                .message("리뷰 수정 성공")
                .data(reviewid)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{memberid}/{reviewid}")
    public ResponseEntity<CustomResponse> removeReview(@PathVariable Long reviewid,@PathVariable Long memberid){
        Review review = reviewService.findOne(reviewid);

        if(!review.getMember().getId().equals(memberid)) {
            CustomResponse response = CustomResponse.builder()
                    .message("글 작성자가 아니라 삭제")
                    .data(reviewid)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        reviewService.remove(reviewid);
        CustomResponse response = CustomResponse.builder()
                .message("리뷰 삭제 성공")
                .data(reviewid)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
