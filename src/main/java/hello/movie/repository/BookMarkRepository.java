package hello.movie.repository;

import hello.movie.dto.MovieListDto;
import hello.movie.model.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    BookMark save(BookMark bookMark);
    void deleteByMemberIdAndMovieId(Long memberId, Long movieId);
    List<MovieListDto> findMovieListDtoByMemberId(Long memberId);
}
