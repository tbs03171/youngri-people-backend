package hello.movie.service;

import hello.movie.dto.MovieDetailDto;
import hello.movie.dto.MovieDto;
import hello.movie.model.*;
import hello.movie.repository.MovieDetailRepository;
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
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieDetailRepository movieDetailRepository;
    private final TMDBApiService tmdbApiService;
    private final PreferredGenreService preferredGenreService;
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * 영화 상세 정보 조회
     */
    @Transactional
    public Optional<MovieDetailDto> getMovieDetailByMovieId(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);

        // 존재하지 않는 영화
        if (movie.isEmpty()) return Optional.empty();

        // 영화 상세 정보 가져오기
        Optional<MovieDetail> movieDetail = movieDetailRepository.findByMovieId(movieId);
        MovieDetailDto movieDetailDto;
        if (movieDetail.isEmpty()) { // 영화 상세 정보가 없다면 TMDB에서 가져와서 저장
            MovieDetail saved = movieDetailRepository.save(tmdbApiService.getMovieDetailById(movie.get().getTmdbId()));
            movieDetailDto = modelMapper.map(saved, MovieDetailDto.class);
        }
        else movieDetailDto = modelMapper.map(movieDetail.get(), MovieDetailDto.class);

        movieDetailDto.setTitle(movie.get().getTitle());
        movieDetailDto.setPosterPath(movie.get().getPosterPath());
        movieDetailDto.setId(movie.get().getId());

        return Optional.of(movieDetailDto);
    }

    /**
     * 현재 상영중인 영화 조회
     */
    public List<MovieDto> getNowPlayingMovies() {
        return processMovieDtos(tmdbApiService.getNowPlayingMovies());
    }

    /**
     * 인기 있는 영화 조회
     */
    public List<MovieDto> getPopularMovies() {
        return processMovieDtos(tmdbApiService.getPopularMovies());
    }

    /**
     * 평점 높은 영화 조회
     */
    public List<MovieDto> getTopRatedMovies() {
        return processMovieDtos(tmdbApiService.getTopRatedMovies());
    }

    /**
     * 개봉 예정인 영화 조회
     */
    public List<MovieDto> getUpcomingMovies() {
        return processMovieDtos(tmdbApiService.getUpcomingMovies());
    }

    /**
     * 제목으로 영화 검색
     */
    public Optional<List<MovieDto>> searchMoviesByTitle(String title) {
        Optional<List<Movie>> movies = tmdbApiService.searchMoviesByTitle(title);
        if (movies.isEmpty()) return Optional.empty(); // 검색 결과 없음
        else return Optional.of(processMovieDtos(movies.get()));
    }

    /**
     * 스탭 또는 배우 이름으로 영화 검색
     */
    public Optional<List<MovieDto>> searchMoviesByPerson(String name) {
        Optional<List<Movie>> movies = tmdbApiService.searchMoviesByPerson(name);
        if (movies.isEmpty()) return Optional.empty(); // 검색 결과 없음
        else return Optional.of(processMovieDtos(movies.get()));
    }

    /**
     * 장르로 영화 조회
     */
    public Optional<List<MovieDto>> getMoviesByGenres(List<String> genres) {
        // genre를 genreId로 변환
        List<Long> genreIds = new ArrayList<>();
        for (String genre : genres) {
            long genreId = Genre.fromString(genre).getId();
            genreIds.add(genreId);
        }

        Optional<List<Movie>> movies = tmdbApiService.getMoviesByGenreIds(genreIds);
        if (movies.isEmpty()) return Optional.empty(); // 검색 결과 없음
        else return Optional.of(processMovieDtos(movies.get()));
    }

    /**
     * 선호 장르 기반 영화 추천
     */
    public Optional<List<MovieDto>> getRecommendedMoviesByPreferredGenre(Long memberId) {
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
    public Optional<List<MovieDto>> getRecommendedMoviesByMbti(Mbti mbti) {
        Optional<List<Object[]>> topRatedMoviesByMbti = movieRepository.findTopRatedMoviesByMbti(mbti);

        if (topRatedMoviesByMbti.get().isEmpty()) return Optional.empty(); // 추천 데이터 없음

        List<MovieDto> movieDtos = new ArrayList<>();
        for (Object[] objects : topRatedMoviesByMbti.get()) {
            Movie movie = (Movie)objects[0];
            movieDtos.add(convertToMovieDto(movie));
        }

        return Optional.of(movieDtos);
    }

    /**
     * 감독 혹은 배우 필모그래피 조회
     */
    public Optional<List<MovieDto>> getFilmographyByPerson(Long personId) {
        Optional<List<Movie>> movies = tmdbApiService.getFilmographyByPerson(personId);
        if (movies.isEmpty()) return Optional.empty(); // 검색 결과 없음
        else return Optional.of(processMovieDtos(movies.get()));
    }

    /**
     * 영화 리스트를 적절히 처리해서 반환
     */
    @Transactional
    public List<MovieDto> processMovieDtos(List<Movie> movies) {
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : movies) {
            // TMDB ID로 영화 조회
            Optional<Movie> movieOptional = movieRepository.findByTmdbId(movie.getTmdbId());

            // DB에 없다면 저장하고 처리
            if (movieOptional.isEmpty()) movieDtos.add(convertToMovieDto(movieRepository.save(movie)));
            else movieDtos.add(convertToMovieDto(movieOptional.get()));
        }
        return movieDtos;
    }

    /**
     * Movie 엔티티를 MovieDto로 변환
     */
    public MovieDto convertToMovieDto(Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }
}
