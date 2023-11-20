package hello.movie.service;

import hello.movie.dto.MovieListDto;
import hello.movie.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final PreferredGenreService preferredGenreService;
    private final MovieService movieService;

    /**
     * 선호 장르 기반 영화 추천
     */
    public Optional<List<MovieListDto>> getRecommendationsByPreferredGenre(Long memberId) {
        // member의 선호 장르 꺼내서
        Optional<List<Genre>> preferredGenres = preferredGenreService.getPreferredGenres(memberId);

        // 선호 장르가 없는 경우
        if (preferredGenres.get().isEmpty()) return Optional.empty();

        // 선호장르를 String 으로 변환
        List<String> genres = new ArrayList<>();
        for (Genre genre : preferredGenres.get()) {
            genres.add(genre.toString());
        }

        // 해당 장르의 영화들 꺼내서 반환
        return movieService.getMoviesByGenres(genres);
    }
}
