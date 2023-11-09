package hello.movie.jwt;

public interface JwtProperties {
    String SECRET = "COS"; //우리 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 60000*60; //10 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
