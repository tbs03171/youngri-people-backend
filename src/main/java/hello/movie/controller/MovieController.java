package hello.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.dto.MovieDTO;
import hello.movie.dto.MovieListDTO;
import hello.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/title")
    public List<MovieListDTO> searchMoviesByTitle(@RequestParam("title") String title) {
        return movieService.searchMoviesByTitle(title);
    }

    @GetMapping("/search/person")
    public List<MovieListDTO> searchMoviesByPerson(@RequestParam("name") String name) {
        return movieService.searchMoviesByPerson(name);
    }
}
