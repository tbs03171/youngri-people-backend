package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.dto.CreateBookMarkDto;
import hello.movie.model.BookMark;
import hello.movie.service.BookMarkService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    //북마크 찜 기능
    @PostMapping("/bookmarks")
    public ResponseEntity<CustomResponse> createBookMark(@RequestBody CreateBookMarkDto createBookMarkDto){
        bookMarkService.saveBookMark(createBookMarkDto);
        CustomResponse response = CustomResponse.builder()
                .message("북마크 찜 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    //memberId로 북마크 movie 조회
    @GetMapping("/bookmarks/{memberId}")
    public ResponseEntity<CustomResponse> getAllBookMarksByMember(@PathVariable Long memberId){
        List<BookMark> bookMarkList = bookMarkService.getAllBookMarksByMember(memberId);
        CustomResponse response = CustomResponse.builder()
                .message("member 북마크 조회 성공")
                .data(bookMarkList)
                .build();
        return ResponseEntity.ok(response);
    }

    //북마크 삭제
    @PostMapping("bookmarks/{bookmarkId}")
    public ResponseEntity<CustomResponse> deleteBookMark(@PathVariable Long bookmarkId){
        Optional<BookMark> bookMark = bookMarkService.getBookMarkById(bookmarkId);

        if(bookMark.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("북마크 삭제 실패")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        bookMarkService.deleteBookMark(bookMark.get());
        CustomResponse response = CustomResponse.builder()
                .message("북마크 삭제 성공")
                .build();
        return ResponseEntity.ok(response);
    }
}
