package hello.movie.repository;

import hello.movie.model.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    BookMark save(BookMark bookMark);
    Optional<BookMark> findById(Long bookmarkId);
    void delete(BookMark bookMark);
    List<BookMark> findMovieByMemberId(Long memberId);
}
