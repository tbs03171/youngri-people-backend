package hello.movie.service;

import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepository memberRepository;

    //로그인
    public Member login(String email, String password){
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.get().getPassword().equals(password))
            return member.get();
        else
            return null;
    }
}
