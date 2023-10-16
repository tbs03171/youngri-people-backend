package hello.movie.controller;

import hello.movie.dto.CreateMemberForm;
import hello.movie.dto.UpdateMemberDto;
import hello.movie.model.Member;
import hello.movie.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public Member getMemberById(@PathVariable("memberId") Long memberId){
        Member member = memberService.findById(memberId);
        return member;
    }

    @PutMapping("/{memberId}")
    public void updateMember(@PathVariable("memberId") Long memberId, @RequestBody UpdateMemberDto updateMemberDto){
        memberService.update(memberId, updateMemberDto);
    }

    @GetMapping("")
    public ResponseEntity<List<Member>> getAllMembers(){
        List<Member> members = memberService.findMembers();
        return ResponseEntity.ok(members);
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id){
        Member findMember = memberService.findOne(id);
        return ResponseEntity.ok(findMember);
    }*/

    @GetMapping("/search/nickname")
    public List<Member> getMemberByNickname(@RequestParam("nickname") String nickname){
        List<Member> findMembers = memberService.findByNickname(nickname);
        return findMembers;
    }

    @GetMapping("/search/email")
    public ResponseEntity<?> getMemberByEmail(@RequestParam("email") String email){
        Optional<Member> findMember = memberService.findByEmail(email);

        if(findMember.isEmpty()){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(findMember);
    }

    @PostMapping("")
    public ResponseEntity<?> createMember(@RequestBody @Valid CreateMemberForm memberForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return ResponseEntity
                    .badRequest()
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
        return ResponseEntity.ok(member);
    }
}
