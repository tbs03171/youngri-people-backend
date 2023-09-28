package hello.movie.service;

import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Long join(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    //회원 전쳬 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 id로 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
