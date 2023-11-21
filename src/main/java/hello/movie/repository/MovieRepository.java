package hello.movie.repository;

import hello.movie.model.Mbti;
import hello.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTmdbId(Long tmdbId);
    Optional<Movie> findByTmdbId(Long tmdbId);

    /**
     * MBTI를 기반으로 사용자들이 본 영화 조회
     */
    @Query("SELECT DISTINCT m FROM Movie m " +
            "WHERE EXISTS (" +
            " SELECT 1 FROM Review r " +
            " JOIN r.member mb " +
            " WHERE m = r.movie AND mb.mbti = :mbti" +
            ")")
    Optional<List<Movie>> findTopRatedMoviesByMbti(@Param("mbti")Mbti mbti);
}
