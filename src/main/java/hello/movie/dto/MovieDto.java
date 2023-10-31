package hello.movie.dto;

import hello.movie.domain.Genre;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class MovieDto {
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private float rating;
    private String posterPath;
    private String trailerPath;
    private List<MovieGenreDto> genres;
    private List<MovieActorDto> actors;
    private List<MovieCrewDto> crew;
}

@Getter @Setter
class MovieGenreDto {
    private Genre genre;
}

@Getter @Setter
class MovieActorDto {
    private String name;
    private String character;
    private String profilePath;
}

@Getter @Setter
class MovieCrewDto {
    private String name;
    private String character;
    private String profilePath;
}