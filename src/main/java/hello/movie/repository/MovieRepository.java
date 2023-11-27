package hello.movie.repository;

import hello.movie.model.Mbti;
import hello.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    /**
     * TMDB ID로 MOVIE ID 조회
     */
    @Query("SELECT m.id FROM Movie m WHERE m.tmdbId = :tmdbId")
    Optional<Long> findIdByTmdbId(Long tmdbId);

    /**
     * MBTI를 기반으로 해당 MBTI를 가진 사용자들이 별점을 높게 평가한 영화 조회
     */
    @Query("SELECT DISTINCT m, AVG(r.reviewRating) AS avgRating " +
            "FROM Movie m " +
            "JOIN Review r ON m.id = r.movie.id " +
            "JOIN Member mb ON r.member.id = mb.id " +
            "WHERE mb.mbti = :mbti " +
            "GROUP BY m.id " +
            "ORDER BY avgRating DESC")
    Optional<List<Object[]>> findTopRatedMoviesByMbti(@Param("mbti")Mbti mbti);
}
