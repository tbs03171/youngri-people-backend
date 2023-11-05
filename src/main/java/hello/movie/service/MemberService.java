package hello.movie.service;

import hello.movie.dto.MemberDto.CreateMemberDto;
import hello.movie.dto.MemberDto.UpdateMemberDto;
import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Optional<Member> join(CreateMemberDto memberDto) {
        Optional<Member> findMember = memberRepository.findByUserId(memberDto.getUserId());

        if (findMember.isEmpty()) {
            Member member = createMemberDtotoMember(memberDto);
            memberRepository.save(member);
            return Optional.of(member);
        } else {
            return Optional.empty();
        }
    }

    //회원 정보 수정
    // update시 입력하지 않은 값 null로 처리되는 문제 발생
    @Transactional
    public Member update(Long memberId, UpdateMemberDto updateMemberDto) {
        Optional<Member> member = findById(memberId);
        return member.get().updateMember(updateMemberDto);
    }

    //회원 전쳬 조회
    public Optional<List<Member>> findMembers() {
        return Optional.of(memberRepository.findAll());
    }

    //회원 id로 조회
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    //회원 nickname으로 조회
    public Optional<List<Member>> findByNickname(String nickname) {
        return memberRepository.findAllByNickname(nickname);
    }

    //회원 email로 조회
    public Optional<Member> findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    public Member createMemberDtotoMember(CreateMemberDto memberDto) {
            Member member = Member.builder()
                    .userId(memberDto.getUserId())
                    .password(bCryptPasswordEncoder.encode(memberDto.getPassword()))
                    .name(memberDto.getName())
                    .phoneNumber(memberDto.getPhoneNumber())
                    .gender(memberDto.getGender())
                    .birthDate(memberDto.getBirthDate())
                    .nickname(memberDto.getNickname())
                    .build();
            return member;
    }
}
