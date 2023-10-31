package hello.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewDTO {

    private Long id;
    private int reviewRating;
    private int likeCount;
    private String comment;

    private LocalDateTime createdDate , modifiedDate;





}
