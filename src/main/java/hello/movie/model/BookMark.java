package hello.movie.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "BOOKMARK")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark {

    @Id
    @GeneratedValue
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
    private Boolean bookmarkStatus;

    public static BookMark createBookMark(Member member, Movie movie) {
        BookMark bookMark = new BookMark();
        bookMark.member = member;
        bookMark.movie = movie;
        bookMark.bookmarkStatus = true;
        return bookMark;
    }
}
