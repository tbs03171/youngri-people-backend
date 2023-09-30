package hello.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.dto.MovieDTO;
import hello.movie.dto.MovieListDTO;
import hello.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/movie")
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
}
