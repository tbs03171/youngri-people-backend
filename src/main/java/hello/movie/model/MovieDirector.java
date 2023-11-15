package hello.movie.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class MovieDirector {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tmdbId;

    @OneToOne(mappedBy = "director", fetch = FetchType.LAZY)
    private Movie movie;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "CREW_ID")
//    private Crew crew;

    private String name;

//    private String character;

    private String profilePath;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
