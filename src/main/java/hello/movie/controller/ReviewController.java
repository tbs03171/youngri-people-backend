package hello.movie.controller;

import hello.movie.dto.ReviewDTO;
import hello.movie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getList(){
        List<ReviewDTO> listOFMovie = reviewService.getListOFMovie();

        return new ResponseEntity<>(listOFMovie,HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO reviewDTO){

        Long findReview = reviewService.register(reviewDTO);
        return new ResponseEntity<>(findReview, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO){

        reviewService.modify(reviewDTO,id);
        return new ResponseEntity<>(id,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> removeReview(@PathVariable Long id){

        reviewService.remove(id);

        return new ResponseEntity<>(id,HttpStatus.OK);
    }


}
