package hello.movie;

import hello.movie.dto.MemberDto.CreateMemberDto;
import hello.movie.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AppStartupRunner implements CommandLineRunner {

    private final MemberService memberService;

    @Override
    public void run(String... args) throws Exception {
        createTestMember();
    }

    /**
     * 기본 계정 생성
     */
    private void createTestMember() {
        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setUserId("test");
        createMemberDto.setPassword("0000");

        memberService.join(createMemberDto);
    }
}
