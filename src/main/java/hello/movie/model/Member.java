package hello.movie.model;

import hello.movie.dto.MemberDto.UpdateMemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
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


    @Builder
    public Member(Long id,String userId, String password, String name, String phoneNumber,
                  Gender gender, Date birthDate, String profilePath, String nickname,
                  Mbti mbti) {
        this.id=id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profilePath = profilePath;
        this.nickname = nickname;
        this.mbti = mbti;
    }

    public Member updateMember(UpdateMemberDto memberDto){
        Optional.ofNullable(memberDto.getNickname()).ifPresent(nickname -> this.nickname = nickname);
        Optional.ofNullable(memberDto.getMbti()).ifPresent(mbti -> this.mbti = mbti);
        Optional.ofNullable(memberDto.getProfilePath()).ifPresent(profilePath -> this.profilePath = profilePath);
        return this;
    }
}
