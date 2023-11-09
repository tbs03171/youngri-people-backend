package hello.movie.repository;

import hello.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTmdbId(Long title);
    Optional<Movie> findByTmdbId(Long tmdbId);
}
