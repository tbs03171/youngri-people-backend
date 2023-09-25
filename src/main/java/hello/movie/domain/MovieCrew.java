package hello.movie.domain;

import jakarta.persistence.*;

@Entity
public class MovieCrew {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREW_ID")
    private Crew crew;

    private String character;
}
