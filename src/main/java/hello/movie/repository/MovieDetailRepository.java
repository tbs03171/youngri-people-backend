package hello.movie.repository;

import hello.movie.model.MovieDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieDetailRepository extends JpaRepository<MovieDetail, Long> {
    /**
     * Movie ID로 MovieDetail 조회
     */
    Optional<MovieDetail> findByMovieId(Long movieId);
}
