package hello.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.dto.MovieDTO;
import hello.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public MovieDTO getMovieDetails(@PathVariable Long id) throws JsonProcessingException {
        return movieService.getMovieById(id);
    }

//    @GetMapping("/popular")
}
