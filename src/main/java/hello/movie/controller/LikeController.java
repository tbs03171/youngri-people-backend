package hello.movie.controller;

import hello.movie.CustomResponse;
import hello.movie.dto.LikeRequestDTO;
import hello.movie.service.LikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@SecurityRequirement(name = "Bearer Authentication")
public class LikeController {
    private final LikeService likeService;
    @PostMapping
    public ResponseEntity<CustomResponse> insert(@RequestBody @Valid LikeRequestDTO likeRequestDTO) throws Exception {
        likeService.insert(likeRequestDTO);
        CustomResponse response = CustomResponse.builder()
                .message("좋아요 성공")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping
    public ResponseEntity<CustomResponse> delete(@RequestBody @Valid LikeRequestDTO likeRequestDTO) {
        likeService.delete(likeRequestDTO);
        CustomResponse response = CustomResponse.builder()
                .message("좋아요 취소")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
