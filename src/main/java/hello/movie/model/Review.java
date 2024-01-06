package hello.movie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;
    private String comment;
    private int reviewRating;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "review",cascade = CascadeType.ALL)
    List<ReviewLike> reviewLikes = new ArrayList<>();



    public void changeGrade(int reviewRating){
        this.reviewRating = reviewRating;
    }

    public void changeComment(String comment){
        this.comment = comment;
    }

    public void changDateTime(LocalDateTime localDateTime){
        this.modifiedDate = localDateTime;
    }



}
