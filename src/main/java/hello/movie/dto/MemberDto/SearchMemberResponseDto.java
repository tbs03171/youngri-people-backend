package hello.movie.dto.MemberDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchMemberResponseDto {

    private String profilePath;
    private String nickname;
    private String userId;
}
