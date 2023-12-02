package hello.movie.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LikeRequestDTO {

    private Long memberId;
    private Long reviewId;

    public LikeRequestDTO(Long memberId, Long reviewId) {
        this.memberId = memberId;
        this.reviewId = reviewId;
    }
}
