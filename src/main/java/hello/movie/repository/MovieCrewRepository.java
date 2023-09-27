package hello.movie.repository;

import hello.movie.domain.MovieCrew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCrewRepository extends JpaRepository<MovieCrew, Long> {
}
