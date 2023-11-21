package hello.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.MovieDto;
import hello.movie.dto.MovieListDto;
import hello.movie.model.Genre;
import hello.movie.model.Mbti;
import hello.movie.model.Member;
import hello.movie.service.MovieService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;


    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "올바르지 않은 Movie ID"),
            @ApiResponse(responseCode = "200", description = "영화 상세 정보 조회 성공")})
    @GetMapping("/{movieId}")
    public ResponseEntity<CustomResponse> getMovieDetails(@PathVariable Long movieId) throws JsonProcessingException {
        Optional<MovieDto> movieDto = movieService.getMovieById(movieId);

        // 조회 실패
        if (movieDto.isEmpty()) {
           CustomResponse response = CustomResponse.builder()
                   .message("올바르지 않은 Movie ID: " + movieId)
                   .build();
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // 조회 성공
        CustomResponse response = CustomResponse.builder()
                .message("영화 상세 정보 조회 성공")
                .data(movieDto.get())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/now-playing")
    public ResponseEntity<CustomResponse> getNowPlayingMovies() {
        List<MovieListDto> movieListDto = movieService.getNowPlayingMovies();

        CustomResponse response = CustomResponse.builder()
                .message("현재 상영중인 영화 목록 조회 성공")
                .data(movieListDto)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/popular")
    public ResponseEntity<CustomResponse> getPopularMovies() {
        List<MovieListDto> movieListDto = movieService.getPopularMovies();

        CustomResponse response = CustomResponse.builder()
                .message("인기 많은 영화 목록 조회 성공")
                .data(movieListDto)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/top-rated")
    public ResponseEntity<CustomResponse> getTopRatedMovies() {
        List<MovieListDto> movieListDto = movieService.getTopRatedMovies();

        CustomResponse response = CustomResponse.builder()
                .message("평점 높은 영화 목록 조회 성공")
                .data(movieListDto)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/upcoming")
    public ResponseEntity<CustomResponse> getUpcomingMovies() {
        List<MovieListDto> movieListDto = movieService.getUpcomingMovies();

        CustomResponse response = CustomResponse.builder()
                .message("개봉 예정인 영화 목록 조회 성공")
                .data(movieListDto)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<CustomResponse> searchMovies(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "person", required = false) String person
    ) {
        Optional<List<MovieListDto>> movieListDto;
        CustomResponse response;

        if (title != null) {
            // 제목으로 영화 검색
            movieListDto = movieService.searchMoviesByTitle(title);
            response = createSearchResponse(title, movieListDto);

        } else if (person != null) {
            // 배우나 감독으로 영화 검색
            movieListDto = movieService.searchMoviesByPerson(person);
            response = createSearchResponse(person, movieListDto);

        } else {
            // 검색어가 없는 경우 인기 있는 영화 목록 반환
            movieListDto = Optional.of(movieService.getPopularMovies());
            response = CustomResponse.builder()
                    .message("검색어를 입력하지 않음. 요즘 인기있는 영화 목록 반환")
                    .data(movieListDto)
                    .build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/genres/{genre}")
    public ResponseEntity<CustomResponse> getMoviesByGenre(@PathVariable String genre) {
        Optional<List<MovieListDto>> movieListDto = movieService.getMoviesByGenres(List.of(new String[]{genre}));
        CustomResponse response = CustomResponse.builder()
                .message(genre + "장르로 영화 조회 성공")
                .data(movieListDto.get())
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/filmography/{personId}")
    public ResponseEntity<CustomResponse> getFilmographyByPerson(@PathVariable Long personId) {
        Optional<List<MovieListDto>> movieListDto = movieService.getFilmographyByPerson(personId);
        CustomResponse response = CustomResponse.builder()
                .message("필모그래피 조회 성공")
                .data(movieListDto.get())
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/recommended-by-genre")
    public ResponseEntity<CustomResponse> getRecommendedMoviesByPreferredGenre(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        Optional<List<MovieListDto>> movieListDto = movieService.getRecommendedMoviesByPreferredGenre(memberId);

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


    @GetMapping("/recommended-by-mbti")
    public ResponseEntity<CustomResponse> getRecommendedMoviesByMbti(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Mbti mbti = principalDetails.getMember().getMbti();

        // MBTI 등록 안한 경우
        if (mbti == null) {
            CustomResponse response = CustomResponse.builder()
                    .message("MBTI가 등록되지 않음")
                    .build();
            return ResponseEntity.ok(response);
        }

        Optional<List<MovieListDto>> movieListDto = movieService.getRecommendedMoviesByMbti(mbti);
        CustomResponse response = CustomResponse.builder()
                .message(mbti + "가 좋아하는 영화 추천 성공")
                .data(movieListDto.get())
                .build();
        return ResponseEntity.ok(response);
    }


    private CustomResponse createSearchResponse(String keyword, Optional<List<MovieListDto>> movieListDto) {
        if (movieListDto.isEmpty()) {
            return CustomResponse.builder()
                    .message("검색 결과 없음: " + keyword)
                    .build();
        } else {
            return CustomResponse.builder()
                    .message(keyword + "으로 영화 검색 성공")
                    .data(movieListDto.get())
                    .build();
        }
    }
}
