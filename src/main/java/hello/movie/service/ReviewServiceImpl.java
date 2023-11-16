package hello.movie.service;

import hello.movie.dto.ReviewDTO;
import hello.movie.model.Member;
import hello.movie.model.Movie;
import hello.movie.model.Review;
import hello.movie.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;


    @Override
    public List<ReviewDTO> getListOFMovie(Long movieid) {
        Movie movie = Movie.builder().id(movieid).build();
        List<Review> result = reviewRepository.findByMovie(movie);
        return result.stream().map(movieReview -> entityToDTO(movieReview)).collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getListOFMember(Long memberid) {
        Member member = Member.builder().id(memberid).build();
        List<Review> result = reviewRepository.findByMember(member);
        return result.stream().map(movieReview -> entityToDTO(movieReview)).collect(Collectors.toList());
    }
    @Override
    public Long register(ReviewDTO reviewDTO){
        Review review = dtoToEntity(reviewDTO);
        reviewRepository.save(review);

        return review.getId();

    }

    @Override
    public void modify(ReviewDTO reviewDTO,Long id){
        Review review= reviewRepository.findById(id).orElseThrow(RuntimeException::new);

        review.changeComment(reviewDTO.getComment());
        review.changeGrade(reviewDTO.getReviewRating());
        review.changDateTime(LocalDateTime.now());
        reviewRepository.save(review);


    }

    @Override
    public void remove(Long id){
        reviewRepository.deleteById(id);
    }

    @Override
    public Review findOne(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(NullPointerException::new);
        return review;
    }



}
