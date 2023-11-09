package hello.movie.service;

import hello.movie.dto.CreateBookMarkDto;
import hello.movie.model.BookMark;
import hello.movie.repository.BookMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;

    public void saveBookMark(CreateBookMarkDto createBookMarkDto) {
        BookMark bookMark = createBookMarkDtotoBookMark(createBookMarkDto);
        bookMarkRepository.save(bookMark);
    }

    private static BookMark createBookMarkDtotoBookMark(CreateBookMarkDto createBookMarkDto) {
        return new BookMark(createBookMarkDto.getMember(), createBookMarkDto.getMovie(), createBookMarkDto.getBookmarkStatus());
    }

    public Optional<BookMark> getBookMarkById(Long bookmarkId) {
        return bookMarkRepository.findById(bookmarkId);
    }

    public void deleteBookMark(BookMark bookMark) {
        bookMarkRepository.delete(bookMark);
    }

    public List<BookMark> getAllBookMarksByMember(Long memberId) {
        return bookMarkRepository.findMovieByMemberId(memberId);
    }

}
