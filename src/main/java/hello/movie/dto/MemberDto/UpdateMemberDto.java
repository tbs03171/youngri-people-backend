package hello.movie.dto.MemberDto;

import hello.movie.model.Mbti;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateMemberDto {

    private String profilePath;
    private String nickname;
    private Mbti mbti;
}
