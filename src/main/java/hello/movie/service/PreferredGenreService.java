package hello.movie.service;

import hello.movie.model.Genre;
import hello.movie.model.Member;
import hello.movie.model.MemberPreferredGenre;
import hello.movie.repository.MemberRepository;
import hello.movie.repository.PreferredGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreferredGenreService {

    private final PreferredGenreRepository preferredGenreRepository;
    private final MemberRepository memberRepository;

    /**
     * 선호 장르 조회
     */
    public List<Genre> getPreferredGenres(Long memberId) {
        // 멤버 가져와서
        Member member = memberRepository.findById(memberId).get();

        // 선호 장르 조회해서
        List<MemberPreferredGenre> memberPreferredGenres = preferredGenreRepository.findByMember(member).get();

        // List<Genre> 형태로 변환
        List<Genre> genres = new ArrayList<>();
        for (MemberPreferredGenre memberPreferredGenre : memberPreferredGenres) {
            genres.add(memberPreferredGenre.getGenre());
        }

        return genres;
    }

    /**
     * 선호 장르 추가
     */
    @Transactional
    public void addPreferredGenre(Long memberId, Long genreId) {
        Member member = memberRepository.findById(memberId).get();
        preferredGenreRepository.save(
                MemberPreferredGenre.builder()
                        .genre(Genre.fromId(genreId))
                        .member(member)
                        .build());
    }

    /**
     * 선호 장르 삭제
     */
    @Transactional
    public void removePreferredGenre(Long memberId, Long genreId) {
        // 멤버 가져와서
        Member member = memberRepository.findById(memberId).get();

        // 선호하는 장르인지 확인
        Optional<MemberPreferredGenre> memberPreferredGenre = preferredGenreRepository.findByMemberAndGenre(member, Genre.fromId(genreId));

        // 선호 장르 삭제
        if (memberPreferredGenre.isPresent()) preferredGenreRepository.delete(memberPreferredGenre.get());
    }

}
