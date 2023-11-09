package hello.movie.repository;

import hello.movie.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Member save(Member member);
    List<Member> findAll();
    Optional<Member> findById(Long id);
    Optional<Member> findByUserId(String userId);
    Optional<List<Member>> findAllByNickname(String nickname);


}
