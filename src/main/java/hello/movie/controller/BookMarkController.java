package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.MovieDto;
import hello.movie.service.BookMarkService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/bookmark")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    //북마크 찜 기능
    @PostMapping("/{movieId}")
    public ResponseEntity<CustomResponse> createBookMark(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long movieId){
        Long memberId = principalDetails.getMember().getId();

        bookMarkService.saveBookMark(memberId, movieId);

        CustomResponse response = CustomResponse.builder()
                .message("북마크 찜 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    //북마크 삭제
    @DeleteMapping("/{movieId}")
    public ResponseEntity<CustomResponse> deleteBookMark(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long movieId){
        Long memberId = principalDetails.getMember().getId();

        bookMarkService.deleteBookMark(memberId, movieId);

        CustomResponse response = CustomResponse.builder()
                .message("북마크 삭제 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    //북마크 상태 확인
    @GetMapping("/status/{movieId}")
    public ResponseEntity<CustomResponse> checkBookMarkStatus(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long movieId){
        Long memberId = principalDetails.getMember().getId();

        boolean status = bookMarkService.isBookMark(memberId, movieId);

        CustomResponse response = CustomResponse.builder()
                .message("북마크 상태 유무")
                .data(status)
                .build();

        return ResponseEntity.ok(response);
    }

    //memberId로 전체 북마크 movie 조회
    @GetMapping("/bookmarks")
    public ResponseEntity<CustomResponse> getAllBookMarks(@AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memberId = principalDetails.getMember().getId();

        List<MovieDto> bookMarkList = bookMarkService.getBookMarksList(memberId);

        CustomResponse response = CustomResponse.builder()
                .message("memberId로 찜한 모든 북마크 조회 성공")
                .data(bookMarkList)
                .build();
        return ResponseEntity.ok(response);
    }
}
