package hello.movie.dto;

import hello.movie.model.Member;
import hello.movie.model.Movie;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class CreateBookMarkDto {
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
    private Integer bookmarkStatus;
}
