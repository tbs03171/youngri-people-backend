package hello.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MovieListDto {
    private Long id;
    private String title;
    private String posterPath;
}
