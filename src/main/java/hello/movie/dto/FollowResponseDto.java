package hello.movie.dto;

import hello.movie.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponseDto {
    private Long id;
    private String profilePath;
    private String nickname;
    private String userId;

    public static FollowResponseDto createFollowResponseDto(Member member) {
        FollowResponseDto followResponseDto = new FollowResponseDto();
        followResponseDto.id = member.getId();
        followResponseDto.profilePath = member.getProfilePath();
        followResponseDto.nickname = member.getNickname();
        followResponseDto.userId = member.getUserId();
        return followResponseDto;
    }
}
