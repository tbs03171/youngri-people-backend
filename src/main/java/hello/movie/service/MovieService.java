package hello.movie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.movie.model.Genre;
import hello.movie.model.Mbti;
import hello.movie.model.Movie;
import hello.movie.dto.MovieDto;
import hello.movie.dto.MovieListDto;
import hello.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TMDBApiService tmdbApiService;
    private final PreferredGenreService preferredGenreService;
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * 영화 상세 정보 조회
     */
    @Transactional
    public Optional<MovieDto> getMovieById(Long movieId) throws JsonProcessingException {
        if (!movieRepository.existsByTmdbId(movieId)) { // 영화 정보가 DB에 없는 경우 TMDB에서 가져와서 저장
            Optional<Movie> movie = tmdbApiService.getMovieById(movieId);
            if (movie.isPresent()) movieRepository.save(movie.get());
        }

        Optional<Movie> movie = movieRepository.findByTmdbId(movieId);

        // 유효하지 않은 movie id
        if (movie.isEmpty()) return Optional.empty();

        return Optional.of(convertToMovieDto(movie.get()));
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
    public Optional<List<MovieListDto>> searchMoviesByTitle(String title) {
        return tmdbApiService.searchMoviesByTitle(title);
    }


    /**
     * 스탭 또는 배우 이름으로 영화 검색
     */
    public Optional<List<MovieListDto>> searchMoviesByPerson(String name) {
        return tmdbApiService.searchMoviesByPerson(name);
    }


    /**
     * 장르로 영화 조회
     */
    public Optional<List<MovieListDto>> getMoviesByGenres(List<String> genres) {
        // genre를 genreId로 변환
        List<Long> genreIds = new ArrayList<>();
        for (String genre : genres) {
            long genreId = Genre.fromString(genre).getId();
            genreIds.add(genreId);
        }
        return tmdbApiService.getMoviesByGenreIds(genreIds);
    }


    /**
     * 선호 장르 기반 영화 추천
     */
    public Optional<List<MovieListDto>> getRecommendedMoviesByPreferredGenre(Long memberId) {
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
        return getMoviesByGenres(genres);
    }


    /**
     * MBTI 기반 영화 추천
     */
    public Optional<List<MovieListDto>> getRecommendedMoviesByMbti(Mbti mbti) {
        Optional<List<Movie>> topRatedMoviesByMbti = movieRepository.findTopRatedMoviesByMbti(mbti);

        List<MovieListDto> movieListDtos = new ArrayList<>();
        for (Movie movie : topRatedMoviesByMbti.get()) {
            movieListDtos.add(convertToMovieListDto(movie));
        }

        return Optional.of(movieListDtos);
    }


    /**
     * 감독 혹은 배우 필모그래피 조회
     */
    public Optional<List<MovieListDto>> getFilmographyByPerson(Long personId) {
        return tmdbApiService.getFilmographyByPerson(personId);
    }


    /**
     * Movie 엔티티를 MovieDto로 변환
     */
    public MovieDto convertToMovieDto(Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }

    /**
     * Movie 엔티티를 MovieListDto로 변환
     */
    public MovieListDto convertToMovieListDto(Movie movie) {
        return modelMapper.map(movie, MovieListDto.class);
    }
}
