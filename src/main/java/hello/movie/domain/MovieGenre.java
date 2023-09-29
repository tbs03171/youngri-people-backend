package hello.movie.domain;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Builder
public class MovieGenre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_GENRE_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
