package hello.movie.service;

import hello.movie.dto.CreateBookMarkDto;
import hello.movie.dto.MovieListDto;
import hello.movie.model.BookMark;
import hello.movie.model.Member;
import hello.movie.model.Movie;
import hello.movie.repository.BookMarkRepository;
import hello.movie.repository.MemberRepository;
import hello.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final MovieService movieService;

    //북마크 저장
    @Transactional
    public void saveBookMark(Long memberId, Long movieId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Movie> movie = movieRepository.findById(movieId);

        BookMark bookMark = BookMark.createBookMark(member.get(), movie.get());
        bookMarkRepository.save(bookMark);
    }

    //북마크 삭제
    @Transactional
    public void deleteBookMark(Long memberId, Long movieId) {
        bookMarkRepository.deleteByMemberIdAndMovieId(memberId, movieId);
    }

    //북마크 상태값 확인
    public boolean isBookMark(Long memberId, Long movieId) {
        return bookMarkRepository.existsByMemberIdAndMovieId(memberId, movieId);
    }

    //내 북마크의 모든 영화 조회
    public List<MovieListDto> getBookMarksList(Long memberId) {
        List<BookMark> bookMarkList = bookMarkRepository.findAllByMemberId(memberId);

        List<MovieListDto> movieListDto = new ArrayList<>();
        for ( BookMark bookMark : bookMarkList) {
            movieListDto.add(movieService.convertToMovieListDto(bookMark.getMovie()));
        }
        return movieListDto;
    }
}
