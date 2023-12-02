package hello.movie.service;

import hello.movie.dto.MemberDto.CreateMemberDto;
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
    @DisplayName("회원 가입 성공")
    void joinMemberSuccess() {
        // given
        CreateMemberDto createMemberDto = createSampleCreateMemberDto();

        // when
        Optional<Member> result = memberService.join(createMemberDto);

        // then
        Assertions.assertThat(result.isPresent()).isTrue();
    }

    private CreateMemberDto createSampleCreateMemberDto() {

        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setUserId("test1");
        createMemberDto.setPassword("0000");
        createMemberDto.setCheckPassword("0000");
        createMemberDto.setName("test1");
        createMemberDto.setPhoneNumber("010-1234-1234");
        createMemberDto.setGender(Gender.MALE);
        createMemberDto.setBirthDate(new Date(2002, 7,7));
        createMemberDto.setNickname("test1");

        return createMemberDto;
    }
}