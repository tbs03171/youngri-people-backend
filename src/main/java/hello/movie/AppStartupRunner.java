package hello.movie;

import hello.movie.dto.MemberDto.CreateMemberDto;
import hello.movie.service.MemberService;
import hello.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AppStartupRunner implements CommandLineRunner {

    private final MemberService memberService;
    private final MovieService movieService;

    @Override
    public void run(String... args) {
        initializeDefaultData();
    }

    /**
     * 초기 데이터 설정
     */
    private void initializeDefaultData()  {
        // 계정 생성
        CreateMemberDto createMemberDto1 = new CreateMemberDto();
        createMemberDto1.setUserId("test");
        createMemberDto1.setPassword("0000");

        CreateMemberDto createMemberDto2 = new CreateMemberDto();
        createMemberDto2.setUserId("test2");
        createMemberDto2.setPassword("0000");

        memberService.join(createMemberDto1);
        memberService.join(createMemberDto2);

        // 영화 데이터
        movieService.getPopularMovies();
        movieService.getUpcomingMovies();
        movieService.getTopRatedMovies();
        movieService.getNowPlayingMovies();
    }
}
