package hello.movie.dto.MemberDto;

import hello.movie.model.Mbti;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberInfoResponseDto {

    private Long id;
    private String profilePath;
    private String name;
    private String nickname;
    private String userId;
    private Mbti mbti;

}
