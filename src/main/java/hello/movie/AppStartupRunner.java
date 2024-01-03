package hello.movie;

import hello.movie.model.*;
import hello.movie.repository.MemberRepository;
import hello.movie.repository.MovieRepository;
import hello.movie.repository.PreferredGenreRepository;
import hello.movie.repository.ReviewRepository;
import hello.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class AppStartupRunner implements CommandLineRunner {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final ReviewRepository reviewRepository;
    private final PreferredGenreRepository preferredGenreRepository;

    @Override
    public void run(String... args) {
        try {
            initializeDefaultData();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 초기 데이터 설정
     */
    private void initializeDefaultData() throws ParseException {
        // 사용자 계정 생성, 영화 로드
        createUsers();
        loadMovies();
        createReview();
    }

    /**
     * 계정 생성
     */
    private void createUsers() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        memberRepository.save(Member.builder()
                .password(bCryptPasswordEncoder.encode("0000"))
                .mbti(Mbti.INFJ)
                .nickname("현정")
                .name("박현정")
                .userId("user")
                .profilePath("2")
                .gender(Gender.FEMALE)
                .birthDate(sdf.parse("2000-02-11"))
                .phoneNumber("010-1234-5678")
                .build());

        memberRepository.save(Member.builder()
                .password(bCryptPasswordEncoder.encode("0000"))
                .mbti(Mbti.ISTJ)
                .nickname("가을")
                .name("조가을")
                .userId("user2")
                .profilePath("3")
                .gender(Gender.FEMALE)
                .birthDate(sdf.parse("2002-11-02"))
                .phoneNumber("010-1234-5678")
                .build());

        memberRepository.save(Member.builder()
                .password(bCryptPasswordEncoder.encode("0000"))
                .mbti(Mbti.INTP)
                .nickname("도형")
                .name("김도형")
                .userId("user3")
                .profilePath("4")
                .gender(Gender.MALE)
                .birthDate(sdf.parse("2002-06-20"))
                .phoneNumber("010-1234-5678")
                .build());

        memberRepository.save(Member.builder()
                .password(bCryptPasswordEncoder.encode("0000"))
                .mbti(Mbti.ENFP)
                .nickname("정민")
                .name("이정민")
                .userId("user4")
                .profilePath("2")
                .gender(Gender.FEMALE)
                .birthDate(sdf.parse("2002-02-11"))
                .phoneNumber("010-1234-5678")
                .build());

        memberRepository.save(Member.builder()
                .password(bCryptPasswordEncoder.encode("0000"))
                .mbti(Mbti.ISFP)
                .nickname("지혜")
                .name("박지혜")
                .userId("user5")
                .profilePath("3")
                .gender(Gender.FEMALE)
                .birthDate(sdf.parse("2000-11-02"))
                .phoneNumber("010-1234-5678")
                .build());

        memberRepository.save(Member.builder()
                .password(bCryptPasswordEncoder.encode("0000"))
                .mbti(Mbti.INFP)
                .nickname("채윤")
                .name("이채윤")
                .userId("user6")
                .profilePath("4")
                .gender(Gender.FEMALE)
                .birthDate(sdf.parse("1999-06-20"))
                .phoneNumber("010-1234-5678")
                .build());

        memberRepository.save(Member.builder()
                .password(bCryptPasswordEncoder.encode("0000"))
                .mbti(Mbti.ESFJ)
                .nickname("길동")
                .name("홍길동")
                .userId("test")
                .profilePath("1")
                .gender(Gender.MALE)
                .birthDate(sdf.parse("1888-04-21"))
                .phoneNumber("010-1234-5678")
                .build());

        preferredGenreRepository.save(preferredGenreRepository.save(MemberPreferredGenre.builder()
                .genre(Genre.ACTION)
                .member(memberRepository.findById(1L).get())
                .build()));

        preferredGenreRepository.save(preferredGenreRepository.save(MemberPreferredGenre.builder()
                .genre(Genre.ANIMATION)
                .member(memberRepository.findById(2L).get())
                .build()));

        preferredGenreRepository.save(preferredGenreRepository.save(MemberPreferredGenre.builder()
                .genre(Genre.ROMANCE)
                .member(memberRepository.findById(3L).get())
                .build()));

        preferredGenreRepository.save(preferredGenreRepository.save(MemberPreferredGenre.builder()
                .genre(Genre.COMEDY)
                .member(memberRepository.findById(4L).get())
                .build()));

        preferredGenreRepository.save(preferredGenreRepository.save(MemberPreferredGenre.builder()
                .genre(Genre.ANIMATION)
                .member(memberRepository.findById(5L).get())
                .build()));

        preferredGenreRepository.save(preferredGenreRepository.save(MemberPreferredGenre.builder()
                .genre(Genre.ROMANCE)
                .member(memberRepository.findById(6L).get())
                .build()));

        preferredGenreRepository.save(preferredGenreRepository.save(MemberPreferredGenre.builder()
                .genre(Genre.COMEDY)
                .member(memberRepository.findById(7L).get())
                .build()));

    }

    /**
     * 리뷰 생성
     */
    private void createReview() {
        reviewRepository.save(Review.builder()
                .member(memberRepository.findById(7L).get())
                .movie(movieRepository.findById(7L).get())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .comment("재밌어요~!")
                .reviewRating(4)
                .build());

        reviewRepository.save(Review.builder()
                .member(memberRepository.findById(1L).get())
                .movie(movieRepository.findById(1L).get())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .comment("별로에요....")
                .reviewRating(1)
                .build());

        reviewRepository.save(Review.builder()
                .member(memberRepository.findById(2L).get())
                .movie(movieRepository.findById(2L).get())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .comment("굿!!!!")
                .reviewRating(3)
                .build());

        reviewRepository.save(Review.builder()
                .member(memberRepository.findById(3L).get())
                .movie(movieRepository.findById(3L).get())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .comment("또 보고싶어요")
                .reviewRating(5)
                .build());
    }

    /**
     * 영화 데이터 로드
     */
    private void loadMovies() {
        movieService.getPopularMovies();
        movieService.getUpcomingMovies();
        movieService.getTopRatedMovies();
        movieService.getNowPlayingMovies();
    }
}

