package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.dto.CreateMemberDto;
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
    public ResponseEntity<CustomResponse> getMemberById(@PathVariable("memberId") Long memberId){
        Optional<Member> member = memberService.findById(memberId);

        CustomResponse response = CustomResponse.builder()
                .message("memberId로 회원 조회 성공")
                .data(member)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<CustomResponse> updateMember(@PathVariable("memberId") Long memberId, @RequestBody UpdateMemberDto updateMemberDto){
        Member member = memberService.update(memberId, updateMemberDto);

        CustomResponse response = CustomResponse.builder()
                .message("회원 정보 수정 성공")
                .data(member)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<CustomResponse> getAllMembers(){
        Optional<List<Member>> members = memberService.findMembers();

        CustomResponse response = CustomResponse.builder()
                .message("모든 멤버 조회 성공")
                .data(members)
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/search/nickname")
    public ResponseEntity<CustomResponse> getMemberByNickname(@RequestParam("nickname") String nickname){
        Optional<List<Member>> findMembers = memberService.findByNickname(nickname);

        if(findMembers.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("일치하는 닉네임이 없습니다.")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        CustomResponse response = CustomResponse.builder()
                .message("닉네임으로 조회 성공")
                .data(findMembers)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/email")
    public ResponseEntity<CustomResponse> getMemberByEmail(@RequestParam("email") String email){
        Optional<Member> findMember = memberService.findByEmail(email);

        if(findMember.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("일치하는 이메일이 없습니다.")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        CustomResponse response = CustomResponse.builder()
                .message("이메일로 조회 성공")
                .data(findMember)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> createMember(@RequestBody @Valid CreateMemberDto memberDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            CustomResponse response = CustomResponse.builder()
                    .message(bindingResult.getFieldError().getDefaultMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Member> member = memberService.join(memberDto);

        if(member.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("이메일이 중복입니다.")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        CustomResponse response = CustomResponse.builder()
                .message("회원가입 성공")
                .data(member)
                .build();
        return ResponseEntity.ok(response);
    }
}
