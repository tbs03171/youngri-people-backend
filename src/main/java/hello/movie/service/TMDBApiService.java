package hello.movie.service;

import com.fasterxml.jackson.databind.JsonNode;
import hello.movie.dto.TmdbDto.TmdbPersonDto;
import hello.movie.dto.TmdbDto.TmdbMovieDetailDto;
import hello.movie.dto.TmdbDto.TmdbMovieDto;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
    public TmdbMovieDetailDto getMovieDetailById(Long movieId) {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/movie/{movieId}")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("append_to_response", "credits");
        builder.uriVariables(Collections.singletonMap("movieId", movieId));

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // JSON 응답 파싱
        JsonNode responseBody = response.getBody();

        // 영화 정보 생성해서 반환
        return parseMovieDetail(responseBody);
    }


    /**
     * 현재 상영중인 영화 조회
     */
    public List<TmdbMovieDto> getNowPlayingMovies() {
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
        return parseMovies(response.getBody().get("results"));
    }


    /**
     * 인기 있는 영화 조회
     */
    public List<TmdbMovieDto> getPopularMovies() {
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
        return parseMovies(response.getBody().get("results"));
    }


    /**
     * 평점 높은 영화 조회
     */
    public List<TmdbMovieDto> getTopRatedMovies() {
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
        return parseMovies(response.getBody().get("results"));
    }


    /**
     * 개봉 예정인 영화 조회
     */
    public List<TmdbMovieDto> getUpcomingMovies() {
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
        return parseMovies(response.getBody().get("results"));
    }


    /**
     * 제목으로 영화 검색
     */
    public Optional<List<TmdbMovieDto>> searchMoviesByTitle(String title) {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/search/movie")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("region", "KR")
                .queryParam("query", title);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.build().toUriString(), JsonNode.class); // 인코딩 두 번 되는 거 조심 !!

        // 검색 결과가 없는 경우
        if (response.getBody().get("total_results").asLong() == 0) return Optional.empty();

        // 영화 리스트 파싱해서 반환
        return Optional.of(parseMovies(response.getBody().get("results")));
    }


    /**
     * 스탭 또는 배우 이름으로 영화 검색
     */
    public Optional<List<TmdbMovieDto>> searchMoviesByPerson(String name) {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/search/person")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("include_adult", "true")
                .queryParam("query", name);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.build().toUriString(), JsonNode.class);

        // 검색 결과가 없는 경우
        if (response.getBody().get("total_results").asLong() == 0) return Optional.empty();

        // 영화 리스트 파싱해서 반환
        return Optional.of(parseMovies(response.getBody().get("results").get(0).get("known_for")));
    }


    /**
     * 장르 ID로 영화 조회
     */
    public Optional<List<TmdbMovieDto>> getMoviesByGenreIds(List<Long> genreIds) {
        // genreId를 |로 연결
        String joinedGenreIds = genreIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));

        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/discover/movie")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("include_adult", "true")
                .queryParam("include_video", "false")
                .queryParam("page", 1)
                .queryParam("sort_by", "popularity.desc")
                .queryParam("with_genres", joinedGenreIds);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // 검색 결과가 없는 경우
        if (response.getBody().get("total_results").asLong() == 0) return Optional.empty();

        // 영화 리스트 파싱해서 반환
        return Optional.of(parseMovies(response.getBody().get("results")));
    }

    /**
     * 감독 혹은 배우 필모그래피 조회
     */
    public Optional<List<TmdbMovieDto>> getFilmographyByPerson(Long personId) {
        // 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/discover/movie")
                .queryParam("api_key", KEY)
                .queryParam("language", "ko-KR")
                .queryParam("include_adult", "true")
                .queryParam("include_video", "false")
                .queryParam("page", 1)
                .queryParam("sort_by", "popularity.desc")
                .queryParam("with_people", personId);

        // HTTP GET 요청
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(), JsonNode.class);

        // 검색 결과가 없는 경우
        if (response.getBody().get("total_results").asLong() == 0) return Optional.empty();

        // 영화 리스트 파싱해서 반환
        return Optional.of(parseMovies(response.getBody().get("results")));
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
        return null; // 예고편 없는 경우
    }


    /**
     * 영화 상세 정보 파싱
     */
    private TmdbMovieDetailDto parseMovieDetail(JsonNode responseBody) {
        // 배우 정보 파싱
        JsonNode actors = responseBody.get("credits").get("cast");
        List<TmdbPersonDto> actorList = new ArrayList<>();
        int cnt = 0;
        for (JsonNode actor : actors) {
            actorList.add(parsePerson(actor));
            cnt++;
            if (cnt > 4) break;
        }

        // 감독 정보 파싱
        JsonNode crewList = responseBody.get("credits").get("crew");
        TmdbPersonDto director = new TmdbPersonDto();
        for (JsonNode crew : crewList) {
            if (crew.get("job").asText().equals("Director")) {
                director = parsePerson(crew);
            }
        }

        // 장르 정보 파싱
        JsonNode genres = responseBody.get("genres");
        List<Genre> genreList = new ArrayList<>();
        for (JsonNode genre : genres) {
            genreList.add(Genre.fromId(genre.get("id").asLong()));
        }

        return TmdbMovieDetailDto.builder()
                .id(responseBody.get("id").asLong())
                .rating(responseBody.get("vote_average").asDouble())
                .overview(responseBody.get("overview").asText())
                .trailerPath(getTrailerById(responseBody.get("id").asLong()))
                .releaseDate(LocalDate.parse(responseBody.get("release_date").asText(), DateTimeFormatter.ISO_DATE))
                .actors(actorList)
                .director(director)
                .genres(genreList)
                .build();
    }

    /**
     * 인물 정보 파싱
     */
    private TmdbPersonDto parsePerson(JsonNode person) {
        return TmdbPersonDto.builder()
                .id(person.get("id").asLong())
                .role(person.has("character") ? person.get("character").asText() : person.get("job").asText())
                .name(person.get("name").asText())
                .profilePath(IMAGE_BASE_URL + person.get("profile_path").asText())
                .build();
    }

//    /**
//     * 감독 정보 파싱
//     */
//    private TmdbPersonDto parseDirector(JsonNode director) {
//        return TmdbPersonDto.builder()
//                .name(director.get("name").asText())
//                .id(director.get("id").asLong())
//                .role("director")
//                .profilePath(IMAGE_BASE_URL + director.get("profile_path").asText())
//                .build();
//    }

//    /**
//     * 장르 정보 파싱
//     */
//    private MovieGenre parseGenre(JsonNode genre) {
//        return MovieGenre.builder()
//                .genre(Genre.fromId(genre.get("id").asLong()))
//                .build();
//    }

    /**
     * 영화 리스트 파싱
     */
    private List<TmdbMovieDto> parseMovies(JsonNode results) {
        List<TmdbMovieDto> movies = new ArrayList<>();
        for (JsonNode result : results) {
            if (!result.has("media_type") || result.get("media_type").asText().equals("movie")) {
                if (result.get("vote_count").asLong() < 10) continue; // 인지도 없는 영화 제외
                movies.add(TmdbMovieDto.builder()
                        .posterPath(IMAGE_BASE_URL + result.get("poster_path").asText())
                        .id(result.get("id").asLong())
                        .title(result.get("title").asText())
                        .build());
            }
        }
        return movies;
    }
}