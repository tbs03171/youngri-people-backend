package hello.movie.dto;

import lombok.Data;

@Data
public class FollowResponseDto {
    private Long id;
    private String profilePath;
    private String nickname;
    private String userId;

    public FollowResponseDto(Long id, String profilePath, String nickname, String userId) {
        this.id = id;
        this.profilePath = profilePath;
        this.nickname = nickname;
        this.userId = userId;
    }
}
