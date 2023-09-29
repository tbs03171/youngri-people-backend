package hello.movie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.domain.Movie;
import hello.movie.dto.MovieDTO;
import hello.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TMDBApiService tmdbApiService;

    // 영화 조회
    public MovieDTO getMovieById(Long id) throws JsonProcessingException {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) { // 영화 정보가 DB에 있는 경우
            Movie movie = movieOptional.get();
            MovieDTO dto = convertToDTO(movie);
            return dto;
        } else { // 영화 정보가 DB에 없는 경우
            Movie movie = tmdbApiService.getMovieById(id);
            movieRepository.save(movie);
            MovieDTO dto = convertToDTO(movie);
            return dto;
        }
    }

    // 영화 등록
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    // 엔티티를 DTO로 변환
    public MovieDTO convertToDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setTitle(movie.getTitle());
        dto.setOverview(movie.getOverview());
        dto.setRating(movie.getRating());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setTrailerPath(movie.getTrailerPath());
        dto.setPosterPath(movie.getPosterPath());

        return dto;
    }
}
