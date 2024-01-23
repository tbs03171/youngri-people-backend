package hello.movie.dto.TmdbDto;

import hello.movie.model.*;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TmdbMovieDetailDto {

    private Long id;

    private Double rating;

    private LocalDate releaseDate;

    private String overview;

    private String trailerPath;

    private List<TmdbPersonDto> actors;

    private TmdbPersonDto director;

    private List<Genre> genres;

    @Builder
    public TmdbMovieDetailDto(Long id, Double rating, LocalDate releaseDate, String overview, String trailerPath, List<TmdbPersonDto> actors, TmdbPersonDto director, List<Genre> genres) {
        this.id = id;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.trailerPath = trailerPath;
        this.actors = actors;
        this.director = director;
        this.genres = genres;
    }
}
