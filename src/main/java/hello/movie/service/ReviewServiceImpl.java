package hello.movie.service;

import hello.movie.domain.Review;
import hello.movie.dto.ReviewDTO;
import hello.movie.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;


    @Override
    public List<ReviewDTO> getListOFMovie() {
        List<Review> result = reviewRepository.findAll();
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
        return null;
    }


}
