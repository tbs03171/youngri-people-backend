package hello.movie.dto;

import hello.movie.model.Mbti;
import lombok.Data;

@Data
public class UpdateMemberDto {

    private String nickname;
    private Mbti mbti;
    private String profilePath;

}
