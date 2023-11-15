package hello.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.MovieDto;
import hello.movie.dto.MovieListDto;
import hello.movie.service.MovieService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        if (!movieDto.isPresent()) {
           CustomResponse response = CustomResponse.builder()
                   .message("올바르지 않은 Movie ID: " + movieId)
                   .build();
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // 조회 성공
        CustomResponse response = CustomResponse.builder()
                .message("영화 상세 정보 조회 성공")
                .data(movieDto)
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
            @RequestParam(name = "person", required = false) String person,
            @RequestParam(name = "genre", required = false) String genre
    ) {
        List<MovieListDto> movieListDto;
        if (title != null) {
            // 제목으로 영화 검색
            movieListDto = movieService.searchMoviesByTitle(title);
        } else if (person != null) {
            // 배우나 감독으로 영화 검색
            movieListDto = movieService.searchMoviesByPerson(person);
        } else {
            // 장르로 영화 검색
            movieListDto = movieService.searchMoviesByGenre(genre);
        }

        CustomResponse response = CustomResponse.builder()
                .message("검색 성공")
                .data(movieListDto)
                .build();
        return ResponseEntity.ok(response);
    }
}
