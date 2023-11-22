package hello.movie.dto.MemberDto;

import hello.movie.model.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CreateMemberDto {

    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    private String checkPassword;
    private String name;
    private String phoneNumber;
    private Gender gender; //[MALE, FEMALE]
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String nickname;
}
