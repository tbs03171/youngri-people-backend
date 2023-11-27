package hello.movie.service;

import hello.movie.model.Genre;
import hello.movie.model.Mbti;
import hello.movie.model.Movie;
import hello.movie.dto.MovieDto;
import hello.movie.dto.MovieListDto;
import hello.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public Optional<MovieDto> getMovieById(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);

        // 유효하지 않은 movie id
        if (movie.isEmpty()) return Optional.empty();

        return Optional.of(convertToMovieDto(movie.get()));
    }


    /**
     * 현재 상영중인 영화 조회
     */
    public List<MovieListDto> getNowPlayingMovies() {
        return processMovieListDtos(tmdbApiService.getNowPlayingMovies());
    }


    /**
     * 인기 있는 영화 조회
     */
    public List<MovieListDto> getPopularMovies() {
        return processMovieListDtos(tmdbApiService.getPopularMovies());
    }


    /**
     * 평점 높은 영화 조회
     */
    public List<MovieListDto> getTopRatedMovies() {
        return processMovieListDtos(tmdbApiService.getTopRatedMovies());
    }


    /**
     * 개봉 예정인 영화 조회
     */
    public List<MovieListDto> getUpcomingMovies() {
        return processMovieListDtos(tmdbApiService.getUpcomingMovies());
    }


    /**
     * 제목으로 영화 검색
     */
    public Optional<List<MovieListDto>> searchMoviesByTitle(String title) {
        Optional<List<MovieListDto>> movieListDtos = tmdbApiService.searchMoviesByTitle(title);
        if (movieListDtos.isEmpty()) return movieListDtos; // 검색 결과 없음
        else return Optional.of(processMovieListDtos(movieListDtos.get()));
    }


    /**
     * 스탭 또는 배우 이름으로 영화 검색
     */
    public Optional<List<MovieListDto>> searchMoviesByPerson(String name) {
        Optional<List<MovieListDto>> movieListDtos = tmdbApiService.searchMoviesByPerson(name);
        if (movieListDtos.isEmpty()) return movieListDtos; // 검색 결과 없음
        else return Optional.of(processMovieListDtos(movieListDtos.get()));
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

        Optional<List<MovieListDto>> movieListDtos = tmdbApiService.getMoviesByGenreIds(genreIds);
        if (movieListDtos.isEmpty()) return movieListDtos; // 검색 결과 없음
        else return Optional.of(processMovieListDtos(movieListDtos.get()));
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
        Optional<List<Object[]>> topRatedMoviesByMbti = movieRepository.findTopRatedMoviesByMbti(mbti);

        if (topRatedMoviesByMbti.get().isEmpty()) return Optional.empty(); // 추천 데이터 없음

        List<MovieListDto> movieListDtos = new ArrayList<>();
        for (Object[] objects : topRatedMoviesByMbti.get()) {
            Movie movie = (Movie)objects[0];
            movieListDtos.add(convertToMovieListDto(movie));
        }

        return Optional.of(movieListDtos);
    }


    /**
     * 감독 혹은 배우 필모그래피 조회
     */
    public Optional<List<MovieListDto>> getFilmographyByPerson(Long personId) {
        Optional<List<MovieListDto>> movieListDtos = tmdbApiService.getFilmographyByPerson(personId);
        if (movieListDtos.isEmpty()) return movieListDtos; // 검색 결과 없음
        else return Optional.of(processMovieListDtos(movieListDtos.get()));
    }

    /**
     * 영화 리스트를 적절히 처리해서 반환
     */
    public List<MovieListDto> processMovieListDtos(List<MovieListDto> movieListDtos) {
        for (MovieListDto dto : movieListDtos) {
            Optional<Long> movieId = movieRepository.findIdByTmdbId(dto.getId()); // TMDB ID로 영화 ID 조회
            if (movieId.isEmpty()) dto.setId(saveMovieDetails(dto.getId())); // DB에 영화가 없다면 TMDB에서 영화 상세 정보 가져와서 저장
            else dto.setId(movieId.get());
        }
        return movieListDtos;
    }

    /**
     * TMDB에서 영화 상세 정보 가져와서 저장
     */
    public Long saveMovieDetails(Long tmdbId) {
        Movie movie = tmdbApiService.getMovieById(tmdbId);
        return movieRepository.save(movie).getId();
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
