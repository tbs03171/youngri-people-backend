package hello.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.dto.MovieDTO;
import hello.movie.dto.MovieListDTO;
import hello.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public MovieDTO getMovieDetails(@PathVariable Long id) throws JsonProcessingException {
        return movieService.getMovieById(id);
    }

    @GetMapping("/now-playing")
    public List<MovieListDTO> getNowPlayingMovies() {
        return movieService.getNowPlayingMovies();
    }

    @GetMapping("/popular")
    public List<MovieListDTO> getPopularMovies() {
        return movieService.getPopularMovies();
    }

    @GetMapping("/top-rated")
    public List<MovieListDTO> getTopRatedMovies() {
        return movieService.getTopRatedMovies();
    }

    @GetMapping("/upcoming")
    public List<MovieListDTO> getUpcomingMovies() {
        return movieService.getUpcomingMovies();
    }


    @GetMapping("/search")
    public List<MovieListDTO> searchMovies(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "person", required = false) String person,
            @RequestParam(name = "genre", required = false) String genre
    ) {
        if (title != null) {
            // 제목으로 영화 검색
            return movieService.searchMoviesByTitle(title);
        } else if (person != null) {
            // 배우나 감독으로 영화 검색
            return movieService.searchMoviesByPerson(person);
        } else if (genre != null) {
            // 장르로 영화 검색
            return movieService.searchMoviesByGenre(genre);
        } else {
            // 파라미터 지정하지 않은 경우
            return Collections.emptyList();
        }
    }

    // 엔드포인트 분리
//    @GetMapping("/search/title")
//    public List<MovieListDTO> searchMoviesByTitle(@RequestParam("title") String title) {
//        return movieService.searchMoviesByTitle(title);
//    }
//
//    @GetMapping("/search/person")
//    public List<MovieListDTO> searchMoviesByPerson(@RequestParam("name") String name) {
//        return movieService.searchMoviesByPerson(name);
//    }
//
//    @GetMapping("/search/genre")
//    public List<MovieListDTO> searchMoviesByGenre(@RequestParam("genre") String genre) {
//        return movieService.searchMoviesByGenre(genre);
//    }
}
