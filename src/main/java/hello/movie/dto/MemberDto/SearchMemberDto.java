package hello.movie.dto.MemberDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchMemberDto {

    private String profilePath;
    private String nickname;
    private String userId;
}
