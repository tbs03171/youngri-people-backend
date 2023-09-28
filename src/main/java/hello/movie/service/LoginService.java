package hello.movie.service;

import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String email, String password){
        Member member = memberRepository.findByEmail(email);
        if(member.getPassword().equals(password))
            return member;
        else
            return null;
    }
}
