package hello.movie.dto;

import hello.movie.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CreateMemberForm {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotBlank
    private String password;
    private String name;
    private String phoneNumber;
    private Gender gender; //[MALE, FEMALE]
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String nickname;

}