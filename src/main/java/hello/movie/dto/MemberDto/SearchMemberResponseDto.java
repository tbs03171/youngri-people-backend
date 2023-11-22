package hello.movie.dto.MemberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class SearchMemberResponseDto {

    private Long id;
    private String profilePath;
    private String nickname;
    private String userId;
}
