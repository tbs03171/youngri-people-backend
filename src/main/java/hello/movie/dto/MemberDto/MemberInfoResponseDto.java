package hello.movie.dto.MemberDto;

import hello.movie.model.Mbti;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberInfoResponseDto {

    private Long id;
    private String profilePath;
    private String name;
    private String nickname;
    private String userId;
    private Mbti mbti;

}
