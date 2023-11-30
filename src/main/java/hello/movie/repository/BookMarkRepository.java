package hello.movie.repository;

import hello.movie.model.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    BookMark save(BookMark bookMark);
    void deleteByMemberIdAndMovieId(Long memberId, Long movieId);
    List<BookMark> findAllByMemberId(Long memberId);
    boolean existsByMemberIdAndMovieId(Long memberId, Long movieId);
}
