package hello.movie.controller.exception;

import lombok.Getter;

@Getter
public class MovieNotFoundException extends RuntimeException {

    private final Long movieId;

    public MovieNotFoundException(Long movieId) {
        this.movieId = movieId;
    }
}
