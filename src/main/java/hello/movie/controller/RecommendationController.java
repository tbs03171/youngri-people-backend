package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.MovieListDto;
import hello.movie.model.Genre;
import hello.movie.service.PreferredGenreService;
import hello.movie.service.RecommendationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * 선호 장르 기반 영화 추천
     */
    @GetMapping("/genre")
    public ResponseEntity<CustomResponse> getRecommendationsByPreferredGenre(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        Optional<List<MovieListDto>> movieListDto = recommendationService.getRecommendationsByPreferredGenre(memberId);

        // 선호 장르 없는 경우
        if (movieListDto.isEmpty()) {
            CustomResponse response = CustomResponse.builder()
                    .message("선호 장르가 등록되지 않음")
                    .build();
            return ResponseEntity.ok(response);
        }

        CustomResponse response = CustomResponse.builder()
                .message("선호 장르 기반 영화 추천 성공")
                .data(movieListDto.get())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * MBTI 기반 영화 추천
     */
    @GetMapping("/mbti")
    public ResponseEntity<CustomResponse> getRecommendationsByMbti() {
        return null;
    }
}
