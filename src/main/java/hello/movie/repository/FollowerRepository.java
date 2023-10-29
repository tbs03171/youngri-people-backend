package hello.movie.repository;

import hello.movie.model.Follow;
import hello.movie.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follow, Long> {


    Follow findByFollowerAndFollowee(Member follower, Member followee);
    @Query("select f.followee from Follow f where f.follower in " +
            "(select m from Member m where m.id=:id)")
    Optional<List<Member>> findAllByFollower(@Param("id") Long id);
}
