package hello.movie.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private Boolean bookmarkStatus;

    public static Bookmark createBookMark(Member member, Movie movie) {
        Bookmark bookMark = new Bookmark();
        bookMark.member = member;
        bookMark.movie = movie;
        bookMark.bookmarkStatus = true;
        return bookMark;
    }
}
