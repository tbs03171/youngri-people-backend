package hello.movie.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeRequestDTO {

    private Long memberId;
    private Long reviewId;

    public LikeRequestDTO(Long memberId, Long reviewId) {
        this.memberId = memberId;
        this.reviewId = reviewId;
    }
}
