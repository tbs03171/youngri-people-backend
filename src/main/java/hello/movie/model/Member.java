package hello.movie.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private long id;
    private String loginId;
    private String password;
    private String nickName;
    private String name;
    private LocalDate birthday;
    private Gender gender; //[M, F]
    private int age;
    private int phoneNumber;
    private String email;

}
