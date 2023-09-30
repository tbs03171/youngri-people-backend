package hello.movie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import hello.movie.domain.*;
import hello.movie.dto.MovieListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TMDBApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tmdb.api.key}")
    private String KEY;
    private final String BASE_URL = "https://api.themoviedb.org/3";
    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w400";
    private final String VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";

    /**
     * 영화 상세 정보 조회
     */
    public Movie getMovieById(Long id) throws JsonProcessingException {
        // 요청 url 생성
        String url = BASE_URL + "/movie/" + id + "?api_key=" + KEY + "&language=ko-KR&append_to_response=credits";

        // GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        // JSON 응답을 객체로 변환
        JsonNode responseBody = response.getBody();

        // 영화 엔티티 생성
        Movie movie = Movie.builder()
                .tmdbId(responseBody.get("id").asLong())
                .overview(responseBody.get("overview").asText())
                .posterPath(IMAGE_BASE_URL + responseBody.get("poster_path").asText())
                .rating(responseBody.get("vote_average").asDouble())
                .releaseDate(LocalDate.parse(responseBody.get("release_date").asText(), DateTimeFormatter.ISO_DATE))
                .trailerPath(getTrailerById(id))
                .title(responseBody.get("title").asText())
                .build();

        // 배우 정보 추가
        JsonNode actorList = responseBody.get("credits").get("cast");
        for (JsonNode actor : actorList) {
            MovieActor movieActor = MovieActor.builder()
                    .tmdbId(actor.get("id").asLong())
                    .name(actor.get("name").asText())
                    .character(actor.get("character").asText())
                    .profilePath(IMAGE_BASE_URL + actor.get("profile_path").asText())
                    .build();

            movie.addMovieActor(movieActor);
        }

        // 스탭 정보 추가
        JsonNode crewList = responseBody.get("credits").get("crew");
        for (JsonNode crew : crewList) {
            MovieCrew movieCrew = MovieCrew.builder()
                    .tmdbId(crew.get("id").asLong())
                    .name(crew.get("name").asText())
                    .character(crew.get("job").asText())
                    .profilePath(IMAGE_BASE_URL + crew.get("profile_path").asText())
                    .build();

            movie.addMovieCrew(movieCrew);
        }

        // 장르 정보 추가
        JsonNode genres = responseBody.get("genres");
        for (JsonNode genre : genres) {
            MovieGenre movieGenre = MovieGenre.builder()
                    .genre(Genre.fromId(genre.get("id").asLong()))
                    .build();

            movie.addMovieGenre(movieGenre);
        }

        return movie;
    }


    /**
     * 현재 상영중인 영화 조회
     */
    public List<MovieListDTO> getNowPlayingMovies() {
        // 요청 url 생성
        String url = BASE_URL + "/movie/now_playing?api_key=" + KEY + "&language=ko&page=1&region=KR";

        // GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        // 영화들 다 넣기
        List<MovieListDTO> movieList = new ArrayList<>();
        JsonNode movies = response.getBody().get("results");
        for (JsonNode movie : movies) {
            MovieListDTO movieListDTO = new MovieListDTO();
            movieListDTO.setId(movie.get("id").asLong());
            movieListDTO.setTitle(movie.get("title").asText());
            movieListDTO.setPosterPath(IMAGE_BASE_URL + movie.get("poster_path").asText());
            movieList.add(movieListDTO);
        }

        return movieList;
    }


    /**
     * 인기 있는 영화 조회
     */
    public List<MovieListDTO> getPopularMovies() {
        // 요청 url 생성
        String url = BASE_URL + "/movie/popular?api_key=" + KEY + "&language=ko&page=1&region=KR";

        // GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        // 영화들 다 넣기
        List<MovieListDTO> movieList = new ArrayList<>();
        JsonNode movies = response.getBody().get("results");
        for (JsonNode movie : movies) {
            MovieListDTO movieListDTO = new MovieListDTO();
            movieListDTO.setId(movie.get("id").asLong());
            movieListDTO.setTitle(movie.get("title").asText());
            movieListDTO.setPosterPath(IMAGE_BASE_URL + movie.get("poster_path").asText());
            movieList.add(movieListDTO);
        }

        return movieList;
    }


    /**
     * 평점 높은 영화 조회
     */
    public List<MovieListDTO> getTopRatedMovies() {
        // 요청 url 생성
        String url = BASE_URL + "/movie/top_rated?api_key=" + KEY + "&language=ko&page=1&region=KR";

        // GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        // 영화들 다 넣기
        List<MovieListDTO> movieList = new ArrayList<>();
        JsonNode movies = response.getBody().get("results");
        for (JsonNode movie : movies) {
            MovieListDTO movieListDTO = new MovieListDTO();
            movieListDTO.setId(movie.get("id").asLong());
            movieListDTO.setTitle(movie.get("title").asText());
            movieListDTO.setPosterPath(IMAGE_BASE_URL + movie.get("poster_path").asText());
            movieList.add(movieListDTO);
        }

        return movieList;
    }


    /**
     * 개봉 예정인 영화 조회
     */
    public List<MovieListDTO> getUpcomingMovies() {
        // 요청 url 생성
        String url = BASE_URL + "/movie/upcoming?api_key=" + KEY + "&language=ko&page=1&region=KR";

        // GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        // 영화들 다 넣기
        List<MovieListDTO> movieList = new ArrayList<>();
        JsonNode movies = response.getBody().get("results");
        for (JsonNode movie : movies) {
            MovieListDTO movieListDTO = new MovieListDTO();
            movieListDTO.setId(movie.get("id").asLong());
            movieListDTO.setTitle(movie.get("title").asText());
            movieListDTO.setPosterPath(IMAGE_BASE_URL + movie.get("poster_path").asText());
            movieList.add(movieListDTO);
        }

        return movieList;
    }


    /**
     * 영화 예고편 조회
     */
    public String getTrailerById(Long id) {
        // 요청 url 생성
        String url = BASE_URL + "/movie/" + id + "/videos?api_key=" + KEY;

        // GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        // JSON 응답에서 예고편 url 꺼내기
        JsonNode resultsNode = response.getBody().get("results");
        for (JsonNode resultNode : resultsNode) {
            String type = resultNode.get("type").asText();
            if ("Trailer".equals(type)) {
                return VIDEO_BASE_URL + resultNode.get("key").asText();
            }
        }
        return null;
    }

}