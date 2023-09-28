package hello.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
