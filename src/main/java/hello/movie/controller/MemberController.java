package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.MemberDto.CreateMemberDto;
import hello.movie.dto.MemberDto.MyPageMemberInfoDto;
import hello.movie.dto.MemberDto.SearchMemberDto;
import hello.movie.dto.MemberDto.UpdateMemberDto;
import hello.movie.model.Member;
import hello.movie.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    //내 마이페이지 정보
    @GetMapping("")
    public ResponseEntity<CustomResponse> getMemberById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = ((PrincipalDetails) authentication.getPrincipal()).getMember().getId();

        Member member = memberService.findById(memberId).get();

        MyPageMemberInfoDto myPageMemberInfoDto = createMyPageMemberInfoDto(member);

        CustomResponse response = CustomResponse.builder()
                .message("memberId로 회원 조회 성공")
                .data(myPageMemberInfoDto)
                .build();
        return ResponseEntity.ok(response);
    }

    //내 정보 업데이트
    @PutMapping("")
    public ResponseEntity<CustomResponse> updateMember(@RequestBody UpdateMemberDto updateMemberDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = ((PrincipalDetails) authentication.getPrincipal()).getMember().getId();

        Member member = memberService.update(memberId, updateMemberDto);

        MyPageMemberInfoDto myPageMemberInfoDto = createMyPageMemberInfoDto(member);
        CustomResponse response = CustomResponse.builder()
                .message("회원 정보 수정 성공")
                .data(myPageMemberInfoDto)
                .build();
        return ResponseEntity.ok(response);
    }

    private static MyPageMemberInfoDto createMyPageMemberInfoDto(Member member) {
        return MyPageMemberInfoDto.builder()
                .profilePath(member.getProfilePath())
                .name(member.getName())
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .mbti(member.getMbti())
                .build();
    }

    //커뮤니티 화면에서 사용자 전체 보기
    @GetMapping("/search")
    public ResponseEntity<CustomResponse> getAllMembers(){
        Optional<List<Member>> members = memberService.findMembers();

        List<SearchMemberDto> searchMemberDtoList = new ArrayList<>();
        for (Member member : members.get()) {
            SearchMemberDto searchMemberDto = createSearchMemberDto(member);
            searchMemberDtoList.add(searchMemberDto);
        }

        CustomResponse response = CustomResponse.builder()
                .message("모든 멤버 조회 성공")
                .data(searchMemberDtoList)
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

        List<SearchMemberDto> searchMemberDtoList = new ArrayList<>();
        for (Member member : findMembers.get()) {
            SearchMemberDto searchMemberDto = createSearchMemberDto(member);
            searchMemberDtoList.add(searchMemberDto);
        }

        CustomResponse response = CustomResponse.builder()
                .message("닉네임으로 조회 성공")
                .data(searchMemberDtoList)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/userId")
    public ResponseEntity<CustomResponse> getMemberByUserId(@RequestParam("userId") String userId){
        Optional<Member> findMember = memberService.findByUserId(userId);

        if(findMember.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("일치하는 아이디가 없습니다.")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        SearchMemberDto searchMemberDto = createSearchMemberDto(findMember.get());

        CustomResponse response = CustomResponse.builder()
                .message("아이디로 조회 성공")
                .data(searchMemberDto)
                .build();
        return ResponseEntity.ok(response);
    }

    private static SearchMemberDto createSearchMemberDto(Member member) {
        return SearchMemberDto.builder()
                .profilePath(member.getProfilePath())
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<CustomResponse> createMember(@RequestBody @Valid CreateMemberDto createMemberDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            CustomResponse response = CustomResponse.builder()
                    .message(bindingResult.getFieldError().getDefaultMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Member> member = memberService.join(createMemberDto);

        if(member.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("아이디가 중복입니다.")
                    .build();
            return ResponseEntity.status(409).body(response);
        }

        CustomResponse response = CustomResponse.builder()
                .message("회원가입 성공")
                .build();
        return ResponseEntity.ok(response);
    }
}
