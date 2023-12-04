package hello.movie.service;

import hello.movie.dto.MemberDto.CreateMemberDto;
import hello.movie.dto.MemberDto.UpdateMemberDto;
import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MemberServiceTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private static MemberService memberService;

    @Test
    void 회원가입_성공() {
        // given
        CreateMemberDto createMemberDto = createCreateMemberDto("testA", "0000");

        // when
        Optional<Member> member = memberService.join(createMemberDto);

        // then
        System.out.println(member.get().getId());
        assertThat(member.get().getUserId()).isEqualTo(createMemberDto.getUserId());
    }

    @Test
    void 회원가입_실페_아이디중복() {
        // given
        CreateMemberDto createMemberDto1 = createCreateMemberDto("testA","0000");
        CreateMemberDto createMemberDto2 = createCreateMemberDto("testB", "0000");

        // when
        Optional<Member> member1 = memberService.join(createMemberDto1);
        Optional<Member> member2 = memberService.join(createMemberDto2);

        // then
        assertThat(member2.isEmpty());
    }

    @Test
    void 회원정보수정_성공(){
        // given
        Member member = Member.builder()
                .id(1L)
                .userId("testA")
                .build();

        UpdateMemberDto updateMemberDto = UpdateMemberDto.builder()
                .nickname("testA")
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        Member updateMember = memberService.update(member.getId(), updateMemberDto);

        // then
        assertThat(updateMember.getNickname()).isEqualTo(updateMemberDto.getNickname());
    }

    @Test
    void 나제외_회원전체조회_성공(){
        // given
        Member memberA = Member.builder()
                .id(1L)
                .userId("testA")
                .build();

        Member memberB = Member.builder()
                .id(2L)
                .userId("testB")
                .build();

        Member memberC = Member.builder()
                .id(3L)
                .userId("testC")
                .build();

        when(memberService.findMembers(memberA.getId())).thenReturn(Arrays.asList(memberB, memberC));

        // when
        List<Member> memberList = memberService.findMembers(memberA.getId());

        // then
        assertThat(memberList).containsExactly(memberB, memberC);
    }

    private static CreateMemberDto createCreateMemberDto(String userId, String password) {
        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setUserId(userId);
        createMemberDto.setPassword(password);
        return createMemberDto;
    }
}