package hello.movie.repository;

import hello.movie.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark save(Bookmark bookMark);
    void deleteByMemberIdAndMovieId(Long memberId, Long movieId);
    List<Bookmark> findAllByMemberId(Long memberId);
    boolean existsByMemberIdAndMovieId(Long memberId, Long movieId);
}
