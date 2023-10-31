package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.dto.LoginDto;
import hello.movie.model.Member;
import hello.movie.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<CustomResponse> login(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            CustomResponse response = CustomResponse.builder()
                    .message(bindingResult.getFieldError().getDefaultMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Member> loginMember = loginService.login(loginDto.getEmail(), loginDto.getPassword());

        if(loginMember.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("이메일 또는 비밀번호가 맞지 않습니다.")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        CustomResponse response = CustomResponse.builder()
                .message("로그인 성공")
                .data(loginMember.get())
                .build();
        return ResponseEntity.ok(response);
    }
}
