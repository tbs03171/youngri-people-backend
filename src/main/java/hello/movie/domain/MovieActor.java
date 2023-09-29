package hello.movie.domain;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Builder
public class MovieActor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ACTOR_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    private String name;

    private String character;

    private String profilePath;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ACTOR_ID")
//    private Actor actor;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    //    public void setActor(Actor actor) {
//        this.actor = actor;
//    }
}
