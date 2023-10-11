package hello.movie.repository;

import hello.movie.model.Follow;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowerRepository {

    private final EntityManager em;

    public void save(Follow follow){
        em.persist(follow);
    }

    public Follow findById(Long id){
        return em.find(Follow.class, id);
    }

}
