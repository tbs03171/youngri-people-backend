package hello.movie.dto;

import lombok.*;

@Getter @Setter
public class MovieDto {
    private Long id;
    private String title;
    private String posterPath;
}
