package hello.movie.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.movie.auth.PrincipalDetails;
import hello.movie.model.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;


// /login 요청했어

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("JwtAutheticationFilter:로그인 시도중");

        //1.username, password 받아서
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Member member = objectMapper.readValue(request.getInputStream(), Member.class);
            System.out.println(member);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getUserId(), member.getPassword());

            //PrincipalDetailsService의 loadUserByUsername()함수가 실행됨
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            //authentication 객체가 session영역에 저장됨. => 로그인이 되었다는 뜻.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨 " + principalDetails.getMember().getUserId());

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //2.정상인지 로그인 시도를 해보는 거에요. authenticationManager로 로그인 시도를 하면!!
        //PrincipalDetailsService가 호출 loadUserByUsername()함수 실행됨.

        //3.PrincipalDetails 세션에 담음 (권한 관리를 위해서 )
        //4.JWT토큰을 만들어서 응답해줌
    }

    //attempAuthentication실행 후 인증이 정상적으로 완료되었으면 successfulAuthentication 함수가 실행되요.
    //JWT 토는을 만들어서 request요청한 사용자에거 JWT토큰을 response해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentiaction 실행됨: 인증이완료되었다는 뜻임");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        //RSA방식은 아니고 Hash암호방식
        String jwtToken = JWT.create()
                .withSubject(JwtProperties.SECRET)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("userId", principalDetails.getMember().getUserId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":\"로그인 성공\"}");
    }
}