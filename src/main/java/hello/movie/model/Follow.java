package hello.movie.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue
    @Column(name = "FOLLOW_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FOLLOWER")
    private Member follower;

    @ManyToOne
    @JoinColumn(name = "FOLLOWEE")
    private Member followee;

    @Temporal(TemporalType.DATE)
    private Date followDate;

    public Follow(Member follower, Member followee) {
        this.follower = follower;
        this.followee = followee;
        this.followDate = new Date();
    }
}
