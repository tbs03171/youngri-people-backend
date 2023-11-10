package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.model.Genre;
import hello.movie.service.PreferredGenreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/preferred-genres")
@RequiredArgsConstructor
public class PreferredGenreController {

    private final PreferredGenreService preferredGenreService;

    /**
     * 선호 장르 조회
     */
    @GetMapping("")
    public ResponseEntity<CustomResponse> getPreferredGenres(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        List<Genre> preferredGenres = preferredGenreService.getPreferredGenres(memberId);

        CustomResponse response = CustomResponse.builder()
                .message("선호 장르 조회 성공")
                .data(preferredGenres)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * 선호 장르 업데이트
     */
    @PutMapping("")
    public ResponseEntity<CustomResponse> updatePreferredGenre(@RequestBody List<Long> genreIds, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        preferredGenreService.updatePreferredGenre(memberId, genreIds);
        CustomResponse response = CustomResponse.builder()
                .message("선호 장르 업데이트 성공")
                .build();
        return ResponseEntity.ok(response);
    }

//    /**
//     * 선호 장르 삭제
//     */
//    @DeleteMapping("")
//    public ResponseEntity<CustomResponse> removePreferredGenre(@RequestBody Long genreId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        Long memberId = principalDetails.getMember().getId();
//        preferredGenreService.removePreferredGenre(memberId, genreId);
//        CustomResponse response = CustomResponse.builder()
//                .message("선호 장르 삭제 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
}
