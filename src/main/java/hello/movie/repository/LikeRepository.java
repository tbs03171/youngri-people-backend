package hello.movie.repository;


import hello.movie.model.Member;
import hello.movie.model.Review;
import hello.movie.model.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<ReviewLike,Long> {

 boolean existsByMemberAndReview(Member member, Review review);

 Optional<ReviewLike> findByMemberAndReview(Member member,Review review);

}
