package hello.movie.dto;

import hello.movie.model.Member;
import lombok.*;

@Getter
@Builder
public class FollowResponseDto {

    private Long id;
    private String profilePath;
    private String nickname;
    private String userId;
}
