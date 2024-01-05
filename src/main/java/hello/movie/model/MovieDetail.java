package hello.movie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_detail_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private MovieDirector director;

    @OneToMany(mappedBy = "movieDetail", cascade = CascadeType.ALL)
    private final List<MovieActor> actors = new ArrayList<>();

    @OneToMany(mappedBy = "movieDetail", cascade = CascadeType.ALL)
    private final List<MovieGenre> genres = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    private LocalDate releaseDate;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private Double rating;

    private String trailerPath;

    /**
     * 연관관계 메서드
     */
    public void addMovieActor(MovieActor movieActor) {
        actors.add(movieActor);
        movieActor.setMovieDetail(this);
    }

    public void addMovieGenre(MovieGenre movieGenre) {
        genres.add(movieGenre);
        movieGenre.setMovieDetail(this);
    }

    public void setDirector(MovieDirector movieDirector) {
        director = movieDirector;
        movieDirector.setMovieDetail(this);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.setMovieDetail(this);
    }

//        public void addMovieCrew(MovieDirector crewMember) {
//        crew.add(crewMember);
//        crewMember.setMovie(this);
//    }
}
