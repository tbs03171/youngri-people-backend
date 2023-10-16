package hello.movie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String email;
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
    public Member(String email, String password, String name, String phoneNumber,
                  Gender gender, Date birthDate, String profilePath, String nickname,
                  Mbti mbti) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profilePath = profilePath;
        this.nickname = nickname;
        this.mbti = mbti;
    }

    public void updateMember(String nickname, Mbti mbti, String profilePath){
        this.nickname = nickname;
        this.mbti = mbti;
        this.profilePath = profilePath;
    }
}
