package hello.movie.repository;

import hello.movie.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
