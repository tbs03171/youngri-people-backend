package hello.movie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDirector {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_director_id")
    private Long id;

    private Long tmdbId;

    @OneToOne(mappedBy = "director", fetch = FetchType.LAZY)
    private MovieDetail movieDetail;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "CREW_ID")
//    private Crew crew;

    private String name;

//    private String character;

    private String profilePath;

    public void setMovieDetail(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }
}
