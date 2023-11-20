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
    public Optional<List<Genre>> getPreferredGenres(Long memberId) {
        // 멤버 가져와서
        Member member = memberRepository.findById(memberId).get();

        // 선호 장르 조회해서
        Optional<List<MemberPreferredGenre>> memberPreferredGenres = preferredGenreRepository.findByMember(member);

        // 선호 장르 없는 경우
        if (memberPreferredGenres.isEmpty()) return Optional.empty();

        // List<Genre> 형태로 변환
        List<Genre> genres = new ArrayList<>();
        for (MemberPreferredGenre memberPreferredGenre : memberPreferredGenres.get()) {
            genres.add(memberPreferredGenre.getGenre());
        }

        return Optional.of(genres);
    }

    /**
     * 선호 장르 업데이트
     */
    @Transactional
    public Optional<List<Genre>> updatePreferredGenre(Long memberId, List<Long> genreIds) {
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

        // 업데이트 결과 선호장르 리스트 반환
        return getPreferredGenres(memberId);
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
