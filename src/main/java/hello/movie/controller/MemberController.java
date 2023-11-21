package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.auth.PrincipalDetails;
import hello.movie.dto.MemberDto.CreateMemberDto;
import hello.movie.dto.MemberDto.MemberInfoResponseDto;
import hello.movie.dto.MemberDto.SearchMemberResponseDto;
import hello.movie.dto.MemberDto.UpdateMemberDto;
import hello.movie.model.Member;
import hello.movie.service.MemberService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    //내 마이페이지 정보
    @ApiResponse(responseCode = "200", description = "내 마이페이지 정보 조회 성공")
    @GetMapping("")
    public ResponseEntity<CustomResponse> getMemberInfoById(@AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memberId = principalDetails.getMember().getId();

        Member member = memberService.findById(memberId).get();

        MemberInfoResponseDto memberInfoResponseDto = createMemberInfoResponseDto(member);

        CustomResponse response = CustomResponse.builder()
                .message("내 마이페이지 정보 조회 성공")
                .data(memberInfoResponseDto)
                .build();
        return ResponseEntity.ok(response);
    }

    //내 마이페이지 정보 업데이트
    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
    @PutMapping("")
    public ResponseEntity<CustomResponse> updateMember(@RequestBody UpdateMemberDto updateMemberDto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memberId = principalDetails.getMember().getId();

        Member member = memberService.update(memberId, updateMemberDto);

        MemberInfoResponseDto memberInfoResponseDto = createMemberInfoResponseDto(member);
        CustomResponse response = CustomResponse.builder()
                .message("회원 정보 수정 성공")
                .data(memberInfoResponseDto)
                .build();
        return ResponseEntity.ok(response);
    }

    private static MemberInfoResponseDto createMemberInfoResponseDto(Member member) {
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .profilePath(member.getProfilePath())
                .name(member.getName())
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .mbti(member.getMbti())
                .build();
    }

    //커뮤니티 화면에서 사용자 전체 보기
    @ApiResponse(responseCode = "200", description = "모든 멤버 정보 조회 성공")
    @GetMapping("/search")
    public ResponseEntity<CustomResponse> getAllMembers(){
        Optional<List<Member>> members = memberService.findMembers();

        List<SearchMemberResponseDto> searchMemberResponseDtoList = new ArrayList<>();
        for (Member member : members.get()) {
            SearchMemberResponseDto searchMemberResponseDto = createSearchMemberResponseDto(member);
            searchMemberResponseDtoList.add(searchMemberResponseDto);
        }

        CustomResponse response = CustomResponse.builder()
                .message("모든 멤버 정보 조회 성공")
                .data(searchMemberResponseDtoList)
                .build();
        return ResponseEntity.ok(response);
    }

    //커뮤니티 화면에서 닉네임으로 사용자 정보 조회
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임으로 사용자 조회 성공"),
            @ApiResponse(responseCode = "400", description = "일치하는 닉네임이 없어 실패")})
    @GetMapping("/search/nickname")
    public ResponseEntity<CustomResponse> getMemberByNickname(@RequestParam("nickname") String nickname){
        Optional<List<Member>> findMembers = memberService.findByNickname(nickname);

        if(findMembers.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("일치하는 닉네임이 없어 실패")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        List<SearchMemberResponseDto> searchMemberResponseDtoList = new ArrayList<>();
        for (Member member : findMembers.get()) {
            SearchMemberResponseDto searchMemberResponseDto = createSearchMemberResponseDto(member);
            searchMemberResponseDtoList.add(searchMemberResponseDto);
        }

        CustomResponse response = CustomResponse.builder()
                .message("닉네임으로 사용자 조회 성공")
                .data(searchMemberResponseDtoList)
                .build();

        return ResponseEntity.ok(response);
    }

    //커뮤니티 화면에서 사용자 아이디로 사용자 정보 조회
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디로 사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "일치하는 사용자 아이디 없어 실패")})
    @GetMapping("/search/userId")
    public ResponseEntity<CustomResponse> getMemberByUserId(@RequestParam("userId") String userId){
        Optional<Member> findMember = memberService.findByUserId(userId);

        if(findMember.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("일치하는 사용자 아이디 없어 실패")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        SearchMemberResponseDto searchMemberResponseDto = createSearchMemberResponseDto(findMember.get());

        CustomResponse response = CustomResponse.builder()
                .message("아이디로 사용자 정보 조회 성공")
                .data(searchMemberResponseDto)
                .build();
        return ResponseEntity.ok(response);
    }

    private static SearchMemberResponseDto createSearchMemberResponseDto(Member member) {
        return SearchMemberResponseDto.builder()
                .id(member.getId())
                .profilePath(member.getProfilePath())
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "아이디 중복"),
            @ApiResponse(responseCode = "400_1", description = "검증 오류로 실패"),
            @ApiResponse(responseCode = "400_2", description = "비밀번호 불일치")})
    @PostMapping("/create")
    public ResponseEntity<CustomResponse> createMember(@RequestBody @Valid CreateMemberDto createMemberDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            CustomResponse response = CustomResponse.builder()
                    .message(bindingResult.getFieldError().getDefaultMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        if(!createMemberDto.getPassword().equals(createMemberDto.getCheckPassword())){
            CustomResponse response = CustomResponse.builder()
                    .message("비밀번호 불일치")
                    .build();

            return ResponseEntity.badRequest().body(response);
        }

        Optional<Member> member = memberService.join(createMemberDto);

        if(member.isEmpty()){
            CustomResponse response = CustomResponse.builder()
                    .message("아이디 중복")
                    .build();

            return ResponseEntity.status(409).body(response);
        }
        CustomResponse response = CustomResponse.builder()
                .message("회원가입 성공")
                .build();
        return ResponseEntity.ok(response);
    }
}
