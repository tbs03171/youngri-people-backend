package hello.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class MovieDTO {
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private float rating;
    private String posterPath;
    private String trailerPath;
}
