package hello.movie.repository;

import hello.movie.dto.FollowResponseDto;
import hello.movie.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follow, Long> {

    Follow save(Follow follow);
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    @Query("select FollowResponseDto(f.followee.id, f.followee.profilePath, f.followee.nickname, f.followee.userId)" +
            " from Follow f where f.follower.id = :id")
    List<FollowResponseDto> findAllFolloweeByFollower(@Param("id") Long memberId);
    Boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followingId);
}
