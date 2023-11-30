package hello.movie.service;

import hello.movie.dto.LikeRequestDTO;
import hello.movie.model.Member;
import hello.movie.model.Review;
import hello.movie.model.ReviewLike;
import hello.movie.repository.LikeRepository;
import hello.movie.repository.MemberRepository;
import hello.movie.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    @Transactional
    public void insert(LikeRequestDTO LikeRequestDTO) throws Exception {

        Member member = memberRepository.findById(LikeRequestDTO.getMemberId())
                .orElseThrow(NullPointerException::new);


      Review review  = reviewRepository.findById(LikeRequestDTO.getReviewId())
               .orElseThrow(NullPointerException::new);

        // 이미 좋아요되어있으면 에러 반환
        if (likeRepository.existsByMemberAndReview(member,review)){
            //TODO 409에러로 변경
            throw new Exception();
        }

        ReviewLike like = ReviewLike.builder()
                .review(review)
                .member(member)
                .build();


        likeRepository.save(like);
    }

    @Transactional
    public void delete(LikeRequestDTO likeRequestDTO) {

        Member member = memberRepository.findById(likeRequestDTO.getMemberId())
                .orElseThrow(NullPointerException::new);

        Review review = reviewRepository.findById(likeRequestDTO.getReviewId())
                .orElseThrow(NullPointerException::new);

        ReviewLike reviewLike = likeRepository.findByMemberAndReview(member,review)
                .orElseThrow(NullPointerException::new);

        likeRepository.delete(reviewLike);
    }

    @Transactional
    public boolean isLike(LikeRequestDTO likeRequestDTO)throws Exception {
        Member member = memberRepository.findById(likeRequestDTO.getMemberId())
                .orElseThrow(NullPointerException::new);


        Review review  = reviewRepository.findById(likeRequestDTO.getReviewId())
                .orElseThrow(NullPointerException::new);
        return likeRepository.existsByMemberAndReview(member,review);
    }

    public LikeRequestDTO createLikeRequestDto(Long memberid,Long reviewid){
        return LikeRequestDTO.builder()
                .memberId(memberid)
                .reviewId(reviewid)
                .build();
    }
}
