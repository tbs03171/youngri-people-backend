package hello.movie.service;

import hello.movie.controller.exception.MovieNotFoundException;
import hello.movie.dto.MovieDetailDto;
import hello.movie.dto.MovieDto;
import hello.movie.dto.TmdbDto.TmdbMovieDetailDto;
import hello.movie.dto.TmdbDto.TmdbMovieDto;
import hello.movie.dto.TmdbDto.TmdbPersonDto;
import hello.movie.model.*;
import hello.movie.repository.MovieDetailRepository;
import hello.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
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
    public MovieDetailDto getMovieDetailByMovieId(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException(movieId));

        // 영화 상세 정보 가져오기
        Optional<MovieDetail> movieDetail = movieDetailRepository.findByMovieId(movieId);
        MovieDetailDto movieDetailDto;
        if (movieDetail.isEmpty()) { // 영화 상세 정보가 없다면 TMDB에서 가져와서 저장
            MovieDetail saved = saveMovieDetail(tmdbApiService.getMovieDetailById(movie.getTmdbId()));
            movieDetailDto = modelMapper.map(saved, MovieDetailDto.class);
        }
        else movieDetailDto = modelMapper.map(movieDetail.get(), MovieDetailDto.class);

        movieDetailDto.setTitle(movie.getTitle());
        movieDetailDto.setPosterPath(movie.getPosterPath());
        movieDetailDto.setId(movie.getId());

        return movieDetailDto;
    }

    /**
     * 현재 상영중인 영화 조회
     */
    @Cacheable(value = "nowPlayingMovies", cacheManager = "contentCacheManager")
    public List<MovieDto> getNowPlayingMovies() {
        return convertToMovieDtos(saveMovies(tmdbApiService.getNowPlayingMovies()));
    }

    /**
     * 인기 있는 영화 조회
     */
    @Cacheable(value = "popularMovies", cacheManager = "contentCacheManager")
    public List<MovieDto> getPopularMovies() {
        return convertToMovieDtos(saveMovies(tmdbApiService.getPopularMovies()));
    }

    /**
     * 평점 높은 영화 조회
     */
    @Cacheable(value = "topRatedMovies", cacheManager = "contentCacheManager")
    public List<MovieDto> getTopRatedMovies() {
        return convertToMovieDtos(saveMovies(tmdbApiService.getTopRatedMovies()));
    }

    /**
     * 개봉 예정인 영화 조회
     */
    @Cacheable(value = "upcomingMovies", cacheManager = "contentCacheManager")
    public List<MovieDto> getUpcomingMovies() {
        return convertToMovieDtos(saveMovies(tmdbApiService.getUpcomingMovies()));
    }

    /**
     * 제목으로 영화 검색
     */
    public Optional<List<MovieDto>> searchMoviesByTitle(String title) {
        Optional<List<TmdbMovieDto>> movies = tmdbApiService.searchMoviesByTitle(title);
        if (movies.isEmpty()) return Optional.empty(); // 검색 결과 없음
        else return Optional.of(convertToMovieDtos(saveMovies(movies.get())));
    }

    /**
     * 스탭 또는 배우 이름으로 영화 검색
     */
    public Optional<List<MovieDto>> searchMoviesByPerson(String name) {
        Optional<List<TmdbMovieDto>> movies = tmdbApiService.searchMoviesByPerson(name);
        if (movies.isEmpty()) return Optional.empty(); // 검색 결과 없음
        else return Optional.of(convertToMovieDtos(saveMovies(movies.get())));
    }

    /**
     * 장르로 영화 조회
     */
    @Cacheable(value = "moviesByGenres", key = "#genres", cacheManager = "contentCacheManager")
    public Optional<List<MovieDto>> getMoviesByGenres(List<String> genres) {
        // genre를 genreId로 변환
        List<Long> genreIds = new ArrayList<>();
        for (String genre : genres) {
            long genreId = Genre.fromString(genre).getId();
            genreIds.add(genreId);
        }

        Optional<List<TmdbMovieDto>> movies = tmdbApiService.getMoviesByGenreIds(genreIds);
        if (movies.isEmpty()) return Optional.empty(); // 검색 결과 없음
        else return Optional.of(convertToMovieDtos(saveMovies(movies.get())));
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
        Optional<List<TmdbMovieDto>> movies = tmdbApiService.getFilmographyByPerson(personId);
        if (movies.isEmpty()) return Optional.empty(); // 검색 결과 없음
        else return Optional.of(convertToMovieDtos(saveMovies(movies.get())));
    }

    /**
     * 영화 세부 정보 저장
     */
    @Transactional
    public MovieDetail saveMovieDetail(TmdbMovieDetailDto tmdbMovieDetailDto) {
        // MovieDetail 생성
        MovieDetail movieDetail = MovieDetail.builder()
                .rating(tmdbMovieDetailDto.getRating())
                .overview(tmdbMovieDetailDto.getOverview())
                .releaseDate(tmdbMovieDetailDto.getReleaseDate())
                .trailerPath(tmdbMovieDetailDto.getTrailerPath())
                .build();

        // 연관관계 (Movie, MovieActor, MovieDirector, MovieGenre)
        movieDetail.setMovie(movieRepository.findByTmdbId(tmdbMovieDetailDto.getId()).get());

        movieDetail.setDirector(MovieDirector.builder()
                .tmdbId(tmdbMovieDetailDto.getDirector().getId())
                .name(tmdbMovieDetailDto.getDirector().getName())
                .profilePath(tmdbMovieDetailDto.getDirector().getProfilePath())
                .build());

        for (TmdbPersonDto actor : tmdbMovieDetailDto.getActors()) {
            movieDetail.addMovieActor(MovieActor.builder()
                    .tmdbId(actor.getId())
                    .name(actor.getName())
                    .role(actor.getRole())
                    .profilePath(actor.getProfilePath())
                    .build());
        }

        for (Genre genre : tmdbMovieDetailDto.getGenres()) {
            movieDetail.addMovieGenre(MovieGenre.builder()
                    .genre(genre)
                    .build());
        }

        return movieDetailRepository.save(movieDetail);
    }

    /**
     * 영화 저장
     */
    @Transactional
    public Movie saveMovie(TmdbMovieDto tmdbMovieDto) {
        return movieRepository.save(Movie.builder()
                .tmdbId(tmdbMovieDto.getId())
                .title(tmdbMovieDto.getTitle())
                .posterPath(tmdbMovieDto.getPosterPath())
                .build());
    }

    /**
     * 영화 리스트 저장
     */
    @Transactional
    public List<Movie> saveMovies(List<TmdbMovieDto> movies) {
        List<Movie> movieList = new ArrayList<>();
        for (TmdbMovieDto tmdbMovieDto : movies) {
            Optional<Movie> movieOptional = movieRepository.findByTmdbId(tmdbMovieDto.getId());
            if (movieOptional.isEmpty())
                movieList.add(saveMovie(tmdbMovieDto));
            else
                movieList.add(movieOptional.get());
        }
        return movieList;
    }

    /**
     * List<Movie>를 List<MovieDto>로 변환
     */
    public List<MovieDto> convertToMovieDtos(List<Movie> movies) {
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : movies) {
            movieDtos.add(convertToMovieDto(movie));
        }
        return movieDtos;
    }

    /**
     * Movie 엔티티를 MovieDto로 변환
     */
    public MovieDto convertToMovieDto(Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }

    /**
     * 영화 랭킹 업데이트
     */
    @Scheduled(cron = "0 0 0 * * MON")
    @CacheEvict(value = {"nowPlayingMovies", "popularMovies", "topRatedMovies", "upcomingMovies", "moviesByGenres"}, allEntries = true, cacheManager = "contentCacheManager")
    public void updateMovieRanking() {
        getNowPlayingMovies();
        getPopularMovies();
        getUpcomingMovies();
        getTopRatedMovies();
    }
}
