package hello.movie.model;

import hello.movie.dto.MemberDto.UpdateMemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userId;
    private String password;
    private String name;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender; //[MALE, FEMALE]

    @Temporal(TemporalType.DATE)
    private Date birthDate;
    private String profilePath;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    public Member updateMember(UpdateMemberDto memberDto){
        Optional.ofNullable(memberDto.getNickname()).ifPresent(nickname -> this.nickname = nickname);
        Optional.ofNullable(memberDto.getMbti()).ifPresent(mbti -> this.mbti = mbti);
        Optional.ofNullable(memberDto.getProfilePath()).ifPresent(profilePath -> this.profilePath = profilePath);
        return this;
    }
}
