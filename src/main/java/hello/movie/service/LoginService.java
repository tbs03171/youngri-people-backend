package hello.movie.service;

import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepository memberRepository;

    //로그인
    public Optional<Member> login(String userId, String password) {
        Optional<Member> member = memberRepository.findByUserId(userId);

        if (member.get().getPassword().equals(password))
            return member;
        else
            return Optional.empty();
    }
}
