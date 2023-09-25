package hello.movie.domain;

import jakarta.persistence.*;

@Entity
public class MovieActor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ACTOR_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTOR_ID")
    private Actor actor;

    private String character;
}
