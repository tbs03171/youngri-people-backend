package hello.movie.service;

import hello.movie.dto.MemberDto.CreateMemberDto;
import hello.movie.dto.MemberDto.UpdateMemberDto;
import hello.movie.model.Gender;
import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 회원가입_성공() {
        // given
        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setUserId("test1");
        createMemberDto.setPassword("0000");

        // when
        Optional<Member> member = memberService.join(createMemberDto);

        // then
        Assertions.assertThat(member.get().getUserId()).isEqualTo(createMemberDto.getUserId());
    }

    @Test
    void 회원가입_실페_아이디중복() {
        // given
        CreateMemberDto createMemberDto1 = new CreateMemberDto();
        createMemberDto1.setUserId("test1");
        createMemberDto1.setPassword("0000");

        CreateMemberDto createMemberDto2 = new CreateMemberDto();
        createMemberDto2.setUserId("test2");
        createMemberDto2.setPassword("0000");

        // when
        Optional<Member> member1 = memberService.join(createMemberDto1);
        Optional<Member> member2 = memberService.join(createMemberDto2);

        // then
        Assertions.assertThat(member2.isEmpty());
    }

    /*@Test
    void 회원정보수정_성공(){
        // given
        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setUserId("test1");
        createMemberDto.setPassword("0000");

        // when
        Optional<Member> member = memberService.join(createMemberDto);

        UpdateMemberDto updateMemberDto = new UpdateMemberDto();



        // then
        Assertions.assertThat(member2.isEmpty());
    }*/
}