package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.dto.LoginDto;
import hello.movie.model.Member;
import hello.movie.service.LoginService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;

    //로그인 서버 검증 처리 다시
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "404", description = "서버 검증으로 실패"),
            @ApiResponse(responseCode = "400", description = "validation 오류로 실패")})
    @PostMapping("/login")
    public ResponseEntity<CustomResponse> login(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult, Authentication authentication){

        if(bindingResult.hasErrors()){
            CustomResponse response = CustomResponse.builder()
                    .message(bindingResult.getFieldError().getDefaultMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Member> loginMember = loginService.login(loginDto.getUserId(), loginDto.getPassword());

        if(loginMember.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("아이디 또는 비밀번호가 맞지 않습니다.")
                    .build();
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }


        CustomResponse response = CustomResponse.builder()
                .message("로그인 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}
