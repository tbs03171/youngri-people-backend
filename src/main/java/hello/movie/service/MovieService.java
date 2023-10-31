package hello.movie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.domain.Genre;
import hello.movie.domain.Movie;
import hello.movie.dto.MovieDto;
import hello.movie.dto.MovieListDto;
import hello.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TMDBApiService tmdbApiService;
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * 영화 상세 정보 조회
     */
    public MovieDto getMovieById(Long id) throws JsonProcessingException {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) { // 영화 정보가 DB에 있는 경우
            Movie movie = movieOptional.get();
            MovieDto movieDTO = convertToMovieDTO(movie);
            return movieDTO;
        } else { // 영화 정보가 DB에 없는 경우
            Movie movie = tmdbApiService.getMovieById(id);
            movieRepository.save(movie);
            MovieDto movieDTO = convertToMovieDTO(movie);
            return movieDTO;
        }
    }

    /**
     * 현재 상영중인 영화 조회
     */
    public List<MovieListDto> getNowPlayingMovies() {
        return tmdbApiService.getNowPlayingMovies();
    }


    /**
     * 인기 있는 영화 조회
     */
    public List<MovieListDto> getPopularMovies() {
        return tmdbApiService.getPopularMovies();
    }


    /**
     * 평점 높은 영화 조회
     */
    public List<MovieListDto> getTopRatedMovies() {
        return tmdbApiService.getTopRatedMovies();
    }


    /**
     * 개봉 예정인 영화 조회
     */
    public List<MovieListDto> getUpcomingMovies() {
        return tmdbApiService.getUpcomingMovies();
    }


    /**
     * 제목으로 영화 검색
     */
    public List<MovieListDto> searchMoviesByTitle(String title) {
        return tmdbApiService.searchMoviesByTitle(title);
    }


    /**
     * 스탭 또는 배우 이름으로 영화 검색
     */
    public List<MovieListDto> searchMoviesByPerson(String name) {
        return tmdbApiService.searchMoviesByPerson(name);
    }


    /**
     * 장르로 영화 검색
     */
    public List<MovieListDto> searchMoviesByGenre(String genre) {
        Long id = (Long) Genre.fromString(genre).getId();
        return tmdbApiService.searchMoviesByGenre(id);
    }


    /**
     * Movie 엔티티를 MovieDTO로 변환
     */
    public MovieDto convertToMovieDTO (Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }


}
