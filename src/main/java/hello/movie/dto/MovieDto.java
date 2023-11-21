package hello.movie.dto;

import hello.movie.model.Genre;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class MovieDto {
    private Long id;
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private Double rating;
    private String posterPath;
    private String trailerPath;
    private List<MovieGenreDto> genres;
    private List<MovieActorDto> actors;
    private MovieDirectorDto director;
}

@Getter @Setter
class MovieGenreDto {
    private Genre genre;
}

@Getter @Setter
class MovieActorDto {
    private String name;
    private Long tmdbId;
    private String character;
    private String profilePath;
}

@Getter @Setter
class MovieDirectorDto {
    private String name;
    private Long tmdbId;
    private String profilePath;
}