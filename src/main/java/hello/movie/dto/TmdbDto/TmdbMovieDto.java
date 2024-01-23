package hello.movie.dto.TmdbDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TmdbMovieDto {

    private Long id;
    private String title;
    private String posterPath;

    @Builder
    public TmdbMovieDto(Long id, String title, String posterPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
    }
}
