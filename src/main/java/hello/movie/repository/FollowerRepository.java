package hello.movie.repository;

import hello.movie.model.Follow;
import hello.movie.model.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowerRepository {

    private final EntityManager em;

    public void save(Follow follow){
        em.persist(follow);
    }

    public void delete(Follow follow){
        em.remove(follow);
    }

    public Follow findById(Long id){
        return em.find(Follow.class, id);
    }

    public Follow findByFollowerAndFollowee(Member follower, Member followee){
        return em.createQuery("select f from Follow f " +
                "where f.follower = :follower " +
                "and f.followee = :followee", Follow.class)
                .setParameter("follower", follower)
                .setParameter("followee", followee)
                .getSingleResult();
    }

    public List<Member> findAllByFollower(Long memberId){
        return em.createQuery("select f.followee from Follow f " +
                        "where f.follower in " +
                        "(select m from Member m " +
                        "where m.id = :memberId)", Member.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    /*public List<Follow> findAllByFollowing(Long memberId){
        return em.createQuery("select f from Follow f where f.followee= : memberId", Follow.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }*/
}
