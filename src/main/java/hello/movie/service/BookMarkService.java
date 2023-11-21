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

    @Transactional
    public void saveBookMark(Long memberId, Long movieId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Movie> movie = movieRepository.findById(movieId);

        BookMark bookMark = BookMark.createBookMark(member.get(), movie.get());
        bookMarkRepository.save(bookMark);
    }

    @Transactional
    public void deleteBookMark(Long memberId, Long movieId) {
        bookMarkRepository.deleteByMemberIdAndMovieId(memberId, movieId);
    }

    public List<MovieListDto> getAllBookMarks(Long memberId) {
        List<Movie> movieList = bookMarkRepository.findMovieAllByMemberId(memberId);
        List<MovieListDto> movieListDto = new ArrayList<>();
        for (Movie movie : movieList) {
            movieListDto.add(movieService.convertToMovieListDto(movie));
        }
        return movieListDto;
    }
}
