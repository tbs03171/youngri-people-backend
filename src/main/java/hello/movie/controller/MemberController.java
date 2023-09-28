package hello.movie.controller;

import hello.movie.dto.CreateMemberForm;
import hello.movie.model.Gender;
import hello.movie.model.Member;
import hello.movie.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/add")
    public ResponseEntity<Void> saveMemberForm(){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/add")
    public ResponseEntity saveMember(@RequestBody @Valid CreateMemberForm memberForm, BindingResult result){

        if(result.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Member member = Member.builder()
                .email(memberForm.getEmail())
                .password(memberForm.getPassword())
                .name(memberForm.getName())
                .phoneNumber(memberForm.getPhoneNumber())
                .gender(memberForm.getGender())
                .birthDate(memberForm.getBirthDate())
                .nickname(memberForm.getNickname())
                .build();
        
        memberService.join(member);
        return ResponseEntity.ok().build();
    }
}
