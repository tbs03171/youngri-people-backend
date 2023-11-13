package hello.movie.service;

import hello.movie.model.Review;
import hello.movie.dto.ReviewDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewService {

    //영화의 모든 리뷰 불러오기

    List<ReviewDTO> getListOFMovie();

    //리뷰 추가
    Long register(ReviewDTO reviewDTO);

    //리뷰 수정
    void modify(ReviewDTO reviewDTO,Long id);

    //리뷰 삭제
    void remove(Long id);

    Review findOne(Long id);

    default Review dtoToEntity(ReviewDTO reviewDTO){
        Review review = Review.builder()
                .id(reviewDTO.getId())
                .reviewRating(reviewDTO.getReviewRating())
                .comment(reviewDTO.getComment())
                .likeCount(reviewDTO.getLikeCount())
                .modifiedDate(LocalDateTime.now())
                .build();

        return review;
    }

    default ReviewDTO entityToDTO(Review review){

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .id(review.getId())
                .reviewRating(review.getReviewRating())
                .likeCount(review.getLikeCount())
                .comment(review.getComment())
                .modifiedDate(review.getModifiedDate())
                .createdDate(review.getCreatedDate())
                .build();
        return reviewDTO;
    }

}
