package hello.movie.auth;

import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//http://localhost:8080/login
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Member> userEntity = memberRepository.findByUserId(userId);
        System.out.println("userEntity:"+userEntity.get().getPassword());
        return new PrincipalDetails(userEntity.get());
    }
}
