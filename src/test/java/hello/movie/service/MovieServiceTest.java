package hello.movie.service;

import hello.movie.dto.MovieDetailDto;
import hello.movie.model.Movie;
import hello.movie.model.MovieDetail;
import hello.movie.repository.MovieDetailRepository;
import hello.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieDetailRepository movieDetailRepository;

    @Mock
    private TMDBApiService tmdbApiService;

    @Mock
    private PreferredGenreService preferredGenreService;

    @InjectMocks
    private MovieService movieService;

    @Test
    void getMovieDetailByMovieId() {

        // given
        Long movieId = 1L;
        Movie movie = Movie.builder()
                .id(movieId)
                .title("Test Movie")
                .posterPath("/test-poster.jpg")
                .build();

        // MovieRepository가 Movie 객체 반환하도록 설정
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        // MovieDetailRepository가 빈 Optional 반환하도록 설정
        when(movieDetailRepository.findByMovieId(movieId)).thenReturn(Optional.empty());
        // TMDBApiService가 가짜 MovieDetail 반환한다고 가정
        when(tmdbApiService.getMovieDetailById(movie.getTmdbId())).thenReturn(new MovieDetail());

        // when
        Optional<MovieDetailDto> result = movieService.getMovieDetailByMovieId(movieId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Movie");
        assertThat(result.get().getPosterPath()).isEqualTo("/test-poster.jpg");
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieDetailRepository, times(1)).findByMovieId(movieId);
        verify(tmdbApiService, times(1)).getMovieDetailById(movie.getTmdbId());
    }

    @Test
    void getNowPlayingMovies() {
    }

    @Test
    void getPopularMovies() {
    }

    @Test
    void getTopRatedMovies() {
    }

    @Test
    void getUpcomingMovies() {
    }

    @Test
    void searchMoviesByTitle() {
    }

    @Test
    void searchMoviesByPerson() {
    }

    @Test
    void getMoviesByGenres() {
    }

    @Test
    void getRecommendedMoviesByPreferredGenre() {
    }

    @Test
    void getRecommendedMoviesByMbti() {
    }

    @Test
    void getFilmographyByPerson() {
    }

    @Test
    void processMovieDtos() {
    }

    @Test
    void convertToMovieDto() {
    }
}