package hello.movie.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieActor> actors;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieCrew> crew;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieGenre> genre;

    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private Float rating;

    private String posterPath;

    private String trailerPath;
}
