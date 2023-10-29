package hello.movie.repository;

import hello.movie.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByEmail(String email);
    Optional<List<Member>> findAllByNickname(String nickname);

}
