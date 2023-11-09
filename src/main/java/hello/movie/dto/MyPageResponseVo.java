package hello.movie.dto;

import hello.movie.model.Mbti;
import lombok.Data;

@Data
public class MyPageResponseVo {

    private String profilePath;
    private String nickname;
    private String email;
    private Mbti mbti;
    //선호장르해시태그
    //내가작성한 리뷰
    //내가 찜 해둔 영화목록
}
