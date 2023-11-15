package hello.movie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import hello.movie.dto.MovieListDto;
import hello.movie.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/movie/{id}")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("append_to_response", "credits");
        builder.uriVariables(Collections.singletonMap("id", id));

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // JSON 응답 파싱
        JsonNode responseBody = response.getBody();

        // 영화 정보 생성
        Movie movie = parseMovieInfo(responseBody);

        return movie;
    }


    /**
     * 현재 상영중인 영화 조회
     */
    public List<MovieListDto> getNowPlayingMovies() {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/movie/now_playing")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("region", "KR")
                .queryParam("page", 1);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // 영화 리스트 파싱해서 반환
        return parseMovieList(response.getBody().get("results"));
    }


    /**
     * 인기 있는 영화 조회
     */
    public List<MovieListDto> getPopularMovies() {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/movie/popular")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("region", "KR")
                .queryParam("page", 1);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // 영화 리스트 파싱해서 반환
        return parseMovieList(response.getBody().get("results"));
    }


    /**
     * 평점 높은 영화 조회
     */
    public List<MovieListDto> getTopRatedMovies() {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/movie/top_rated")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("region", "KR")
                .queryParam("page", 1);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // 영화 리스트 파싱해서 반환
        return parseMovieList(response.getBody().get("results"));
    }


    /**
     * 개봉 예정인 영화 조회
     */
    public List<MovieListDto> getUpcomingMovies() {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/movie/upcoming")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("region", "KR")
                .queryParam("page", 1);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // 영화 리스트 파싱해서 반환
        return parseMovieList(response.getBody().get("results"));
    }


    /**
     * 제목으로 영화 검색
     */
    public List<MovieListDto> searchMoviesByTitle(String title) {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/search/movie")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("region", "KR")
                .queryParam("query", title);

        // HTTP GET 요청
//        System.out.println(builder.build().toUriString());
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.build().toUriString(), JsonNode.class); // 인코딩 두 번 되는 거 조심 !!

        // 영화 리스트 파싱해서 반환
        return parseMovieList(response.getBody().get("results"));
    }


    /**
     * 스탭 또는 배우 이름으로 영화 검색
     */
    public List<MovieListDto> searchMoviesByPerson(String name) {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/search/person")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("include_adult", "true")
                .queryParam("query", name);

        // HTTP GET 요청
//        System.out.println(builder.build().toUriString());
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.build().toUriString(), JsonNode.class);

        // 영화 리스트 파싱해서 반환
        return parseMovieList(response.getBody().get("results").get(0).get("known_for"));
    }


    /**
     * 장르로 영화 검색
     */
    public List<MovieListDto> searchMoviesByGenre(Long id) {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/discover/movie")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("include_adult", "true")
                .queryParam("include_video", "false")
                .queryParam("page", 1)
                .queryParam("sort_by", "popularity.desc")
                .queryParam("with_genres", id);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // 영화 리스트 파싱해서 반환
        return parseMovieList(response.getBody().get("results"));
    }


    /**
     * 영화 예고편 조회
     */
    public String getTrailerById(Long id) {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/movie/{id}/videos")
                .queryParam("api_key", KEY);
        builder.uriVariables(Collections.singletonMap("id", id));

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // JSON 응답에서 예고편 URL 꺼내기
        JsonNode results = response.getBody().get("results");
        for (JsonNode result : results) {
            String type = result.get("type").asText();
            if ("Trailer".equals(type)) {
                return VIDEO_BASE_URL + result.get("key").asText();
            }
        }
        return null;
    }


    // 영화 상세 정보 파싱
    private Movie parseMovieInfo(JsonNode responseBody) {
        Movie movie = Movie.builder()
                .tmdbId(responseBody.get("id").asLong())
                .overview(responseBody.get("overview").asText())
                .posterPath(IMAGE_BASE_URL + responseBody.get("poster_path").asText())
                .rating(responseBody.get("vote_average").asDouble())
                .releaseDate(LocalDate.parse(responseBody.get("release_date").asText(), DateTimeFormatter.ISO_DATE))
                .title(responseBody.get("title").asText())
                .trailerPath(getTrailerById(responseBody.get("id").asLong()))
                .build();

        // 배우 정보 추가
        JsonNode actorList = responseBody.get("credits").get("cast");
        for (JsonNode actor : actorList) {
            movie.addMovieActor(parseActor(actor));
        }

        // 감독 정보 추가
        JsonNode crewList = responseBody.get("credits").get("crew");
        for (JsonNode crew : crewList) {
            if (crew.get("job").asText().equals("Director")) {
                movie.setDirector(parseDirector(crew));
            }
        }


//        // 스탭 정보 추가
//        JsonNode crewList = responseBody.get("credits").get("crew");
//        for (JsonNode crew : crewList) {
//            movie.addMovieCrew(parseCrew(crew));
//        }

        // 장르 정보 추가
        JsonNode genreList = responseBody.get("genres");
        for (JsonNode genre : genreList) {
            movie.addMovieGenre(parseGenre(genre));
        }

        return movie;
    }


    // 배우 정보 파싱
    private MovieActor parseActor(JsonNode actor) {
        MovieActor movieActor = MovieActor.builder()
                .tmdbId(actor.get("id").asLong())
                .name(actor.get("name").asText())
                .character(actor.get("character").asText())
                .profilePath(IMAGE_BASE_URL + actor.get("profile_path").asText())
                .build();
        return movieActor;
    }

    // 감독 정보 파싱
    private MovieDirector parseDirector(JsonNode director) {
        MovieDirector movieDirector = MovieDirector.builder()
                .tmdbId(director.get("id").asLong())
                .name(director.get("name").asText())
                .profilePath(IMAGE_BASE_URL + director.get("profile_path").asText())
                .build();
        return movieDirector;
    }


//    // 스탭 정보 파싱
//    private MovieDirector parseCrew(JsonNode crew) {
//        MovieDirector movieDirector = MovieDirector.builder()
//                .tmdbId(crew.get("id").asLong())
//                .name(crew.get("name").asText())
//                .character(crew.get("job").asText())
//                .profilePath(IMAGE_BASE_URL + crew.get("profile_path").asText())
//                .build();
//        return movieDirector;
//    }


    // 장르 정보 파싱
    private MovieGenre parseGenre(JsonNode genre) {
        MovieGenre movieGenre = MovieGenre.builder()
                .genre(Genre.fromId(genre.get("id").asLong()))
                .build();
        return movieGenre;
    }


    // 영화 리스트 파싱
    private List<MovieListDto> parseMovieList(JsonNode movies) {
        List<MovieListDto> movieList = new ArrayList<>();
        for (JsonNode movie : movies) {
            MovieListDto movieListDTO = new MovieListDto();
            movieListDTO.setId(movie.get("id").asLong());
            movieListDTO.setTitle(movie.get("title").asText());
            movieListDTO.setPosterPath(IMAGE_BASE_URL + movie.get("poster_path").asText());
            movieList.add(movieListDTO);
        }
        return movieList;
    }
}