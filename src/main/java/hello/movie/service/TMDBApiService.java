package hello.movie.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.movie.domain.*;
import hello.movie.repository.MovieRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TMDBApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MovieRepository movieRepository;

    @Value("${tmdb.api.key}")
    private String KEY;
    private final String BASE_URL = "https://api.themoviedb.org/3";
    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w400";
    private final String VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";

    // 영화 상세 정보 조회
    public Movie getMovieById(Long id) throws JsonProcessingException {
        // 요청 url 생성
        String url = BASE_URL + "/movie/" + id + "?api_key=" + KEY + "&language=ko-KR&append_to_response=credits";

        // GET 요청
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // JSON 응답을 객체로 변환
        objectMapper.readValue(response.getBody(), MovieDetailResponse.class);
        MovieDetailResponse movieDetail = objectMapper.readValue(response.getBody(), MovieDetailResponse.class);

        // MovieDetailResponse 객체를 Movie 객체로 변환 (데이터 가공)
        Movie movie = Movie.builder()
                .tmdbId(movieDetail.getId())
                .overview(movieDetail.getOverview())
                .posterPath(IMAGE_BASE_URL + movieDetail.getPosterPath())
                .rating(Float.parseFloat(movieDetail.getRating()))
                .releaseDate(LocalDate.parse(movieDetail.getReleaseDate(), DateTimeFormatter.ISO_DATE))
                        .trailerPath(VIDEO_BASE_URL + getTrailerById(id))
                                .title(movieDetail.getTitle())
                                        .build();

        // 배우 정보 추가
        for (MovieDetailResponse.ActorResponse actorResponse : movieDetail.getCredits().getActorResponses()) {
            MovieActor movieActor = MovieActor.builder()
                    .name(actorResponse.getName())
                    .character(actorResponse.getCharacter())
                    .profilePath(IMAGE_BASE_URL + actorResponse.getProfilePath())
                    .build();

            movie.addMovieActor(movieActor);
        }

        // 스탭 정보 추가
        for (MovieDetailResponse.CrewResponse crewResponse : movieDetail.getCredits().getCrewResponse()) {
            MovieCrew movieCrew = MovieCrew.builder()
                    .name(crewResponse.getName())
                    .character(crewResponse.getJob())
                    .profilePath(IMAGE_BASE_URL + crewResponse.getProfilePath())
                    .build();

            movie.addMovieCrew(movieCrew);
        }

        // 장르 정보 추가
        for (MovieDetailResponse.GenreResponse genreResponse : movieDetail.getGenreResponses()) {
            MovieGenre movieGenre = MovieGenre.builder()
                    .genre(Genre.fromId(genreResponse.getId()))
                    .build();
            movie.addMovieGenre(movieGenre);
        }

        return movie;
    }

    // 영화 예고편 조회
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
                return resultNode.get("key").asText();
            }
        }

        return null;
    }


//    public List<Movie> getPopularMovies() {
//        String url = tmdbBaseUrl + "/movie/popular";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("api_key", tmdbApiKey);
//
//        ResponseEntity<MovieListResponse> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                null,
//                MovieListResponse.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody().getResults();
//        } else {
//            // 에러 처리
//            return Collections.emptyList();
//        }
//    }
//
//    // 다른 TMDB API 엔드포인트와 메서드 추가
    //



}

/**
 * MovieDetail JSON 응답 객체
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
class MovieDetailResponse {
    @JsonProperty("id")
    private long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("vote_average")
    private String rating;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("genres")
    private List<GenreResponse> genreResponses;

    @JsonProperty("credits")
    private CreditResponse credits;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter @Setter
    static class GenreResponse {
        @JsonProperty("id")
        private long id;

        @JsonProperty("name")
        private String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter @Setter
    static class CreditResponse {
        @JsonProperty("cast")
        private List<ActorResponse> actorResponses;

        @JsonProperty("crew")
        private List<CrewResponse> crewResponse;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter @Setter
    static class ActorResponse {
        @JsonProperty("id")
        private long id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("character")
        private String character;

        @JsonProperty("profile_path")
        private String profilePath;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter @Setter
    static class CrewResponse {
        @JsonProperty("id")
        private long id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("job")
        private String job;

        @JsonProperty("profile_path")
        private String profilePath;
    }
}

