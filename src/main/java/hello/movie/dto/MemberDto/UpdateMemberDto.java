package hello.movie.dto.MemberDto;

import hello.movie.model.Mbti;
import lombok.Data;

@Data
public class UpdateMemberDto {

    // update 가능한 속성들??
    private String profilePath;
    private String nickname;
    private Mbti mbti;

}
