package hello.movie.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark {

    @Id
    @GeneratedValue
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
    private Integer bookmarkStatus;

    public BookMark(Member member, Movie movie, Integer bookmarkStatus) {
        this.member = member;
        this.movie = movie;
        this.bookmarkStatus = bookmarkStatus;
    }
}
