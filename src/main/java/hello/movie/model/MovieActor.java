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
public class MovieActor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ACTOR_ID")
    private Long id;

    private Long tmdbId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_DETAIL_ID")
    private MovieDetail movieDetail;

    private String name;

    private String character;

    private String profilePath;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ACTOR_ID")
//    private Actor actor;

    public void setMovieDetail(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }

    //    public void setActor(Actor actor) {
//        this.actor = actor;
//    }
}
