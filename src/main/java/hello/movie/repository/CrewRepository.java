package hello.movie.repository;

import hello.movie.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {
}
