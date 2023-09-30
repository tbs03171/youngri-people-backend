package hello.movie.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    private Long id;

    private Long tmdbId;

    private String title;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private final List<MovieActor> actors = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private final List<MovieCrew> crew = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private final List<MovieGenre> genres = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    private LocalDate releaseDate;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private Double rating;

    private String posterPath;

    private String trailerPath;

    /**
     * 연관관계 메서드
     */
    public void addMovieActor(MovieActor movieActor) {
        actors.add(movieActor);
        movieActor.setMovie(this);
    }


    public void addMovieCrew(MovieCrew crewMember) {
        crew.add(crewMember);
        crewMember.setMovie(this);
    }

    public void addMovieGenre(MovieGenre movieGenre) {
        genres.add(movieGenre);
        movieGenre.setMovie(this);
    }
}
