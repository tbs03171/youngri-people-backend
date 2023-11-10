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
     * 선호 장르 업데이트
     */
    @Transactional
    public void updatePreferredGenre(Long memberId, List<Long> genreIds) {
        // 기존 선호 장르 모두 삭제
        Member member = memberRepository.findById(memberId).get();
        preferredGenreRepository.deleteByMember(member);

        // 새로운 선호 장르로 업데이트
        for (Long genreId : genreIds) {
            preferredGenreRepository.save(MemberPreferredGenre.builder()
                    .genre(Genre.fromId(genreId))
                    .member(member)
                    .build());
        }
    }

//    /**
//     * 선호 장르 삭제
//     */
//    @Transactional
//    public void removePreferredGenre(Long memberId, Long genreId) {
//        // 멤버 가져와서
//        Member member = memberRepository.findById(memberId).get();
//
//        // 선호하는 장르인지 확인
//        Optional<MemberPreferredGenre> memberPreferredGenre = preferredGenreRepository.findByMemberAndGenre(member, Genre.fromId(genreId));
//
//        // 선호 장르 삭제
//        if (memberPreferredGenre.isPresent()) preferredGenreRepository.delete(memberPreferredGenre.get());
//    }

}
