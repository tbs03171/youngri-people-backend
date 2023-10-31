package hello.movie.dto;

import hello.movie.domain.Genre;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class MovieDTO {
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private float rating;
    private String posterPath;
    private String trailerPath;
    private List<MovieGenreDTO> genres;
    private List<MovieActorDTO> actors;
    private List<MovieCrewDTO> crew;
}

@Getter @Setter
class MovieGenreDTO {
    private Genre genre;
}

@Getter @Setter
class MovieActorDTO {
    private String name;
    private String character;
    private String profilePath;
}

@Getter @Setter
class MovieCrewDTO {
    private String name;
    private String character;
    private String profilePath;
}