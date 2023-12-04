package hello.movie.repository;

import hello.movie.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Member save(Member member);
    List<Member> findByIdNot(Long memberId);
    Optional<Member> findById(Long id);
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByUserIdAndIdNot(String userId, Long memberId);
    List<Member> findByNicknameAndIdNot(String nickname, Long memberId);
    void deleteAll();
}
