package hello.movie.controller;

import hello.movie.dto.CreateMemberForm;
import hello.movie.dto.UpdateMemberForm;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<List<Member>> getAllMembers(){
        List<Member> members = memberService.findMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id){
        Member findMember = memberService.findOne(id);
        return ResponseEntity.ok(findMember);
    }

    @PostMapping("")
    public ResponseEntity<?> createMember(@RequestBody @Valid CreateMemberForm memberForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest()
                    .body(bindingResult.getAllErrors());
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

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody UpdateMemberForm memberForm){
        return ResponseEntity.ok().build();

    }
}
