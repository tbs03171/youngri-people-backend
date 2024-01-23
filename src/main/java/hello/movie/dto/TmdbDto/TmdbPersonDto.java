package hello.movie.dto.TmdbDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TmdbPersonDto {

    private Long id;
    private String name;
    private String role;
    private String profilePath;

    @Builder
    public TmdbPersonDto(Long id, String name, String role, String profilePath) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.profilePath = profilePath;
    }
}
