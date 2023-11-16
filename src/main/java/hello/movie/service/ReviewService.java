package hello.movie.service;

import hello.movie.dto.ReviewDTO;
import hello.movie.model.Member;
import hello.movie.model.Movie;
import hello.movie.model.Review;

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
                .member(Member.builder().id(reviewDTO.getMid()).build())
                .comment(reviewDTO.getComment())
                .likeCount(reviewDTO.getLikeCount())
                .modifiedDate(LocalDateTime.now())
                .movie(Movie.builder().id(reviewDTO.getMno()).build())
                .build();

        return review;
    }

    default ReviewDTO entityToDTO(Review review){

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .id(review.getId())
                .reviewRating(review.getReviewRating())
                .likeCount(review.getLikeCount())
                .comment(review.getComment())
                .mid(review.getMember().getId())
                .nickname(review.getMember().getNickname())
                .userid(review.getMember().getUserId())
                .modifiedDate(review.getModifiedDate())
                .createdDate(review.getCreatedDate())
                .mno(review.getMovie().getId())
                .build();

        return reviewDTO;
    }

}
