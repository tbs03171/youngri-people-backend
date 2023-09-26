package hello.movie.service;

import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    //회원 이름 수정
    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        memberRepository.update(member);
    }

}
