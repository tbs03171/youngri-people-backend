package hello.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.MovieDto;
import hello.movie.dto.MovieListDto;
import hello.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;


    @GetMapping("/{movieId}")
    public ResponseEntity<CustomResponse> getMovieDetails(@AuthenticationPrincipal PrincipalDetails principalDetails) throws JsonProcessingException {
        Long movieId = principalDetails.getMember().getId();

        MovieDto movieDto = movieService.getMovieById(movieId);

        CustomResponse response = CustomResponse.builder()
                .message("movieId로 영화 조회 성공")
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
