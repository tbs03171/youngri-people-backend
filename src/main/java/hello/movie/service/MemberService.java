package hello.movie.service;

import hello.movie.dto.MemberDto.CreateMemberDto;
import hello.movie.dto.MemberDto.MemberInfoResponseDto;
import hello.movie.dto.MemberDto.SearchMemberResponseDto;
import hello.movie.dto.MemberDto.UpdateMemberDto;
import hello.movie.model.Member;
import hello.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Optional<Member> join(CreateMemberDto memberDto) {
        Optional<Member> findMember = memberRepository.findByUserId(memberDto.getUserId());

        if (findMember.isEmpty()) {
            Member member = CreateMemberDtoToMember(memberDto);
            memberRepository.save(member);
            return Optional.of(member);
        } else {
            return Optional.empty();
        }
    }

    //회원 정보 수정
    @Transactional
    public Member update(Long memberId, UpdateMemberDto updateMemberDto) {
        Optional<Member> member = findById(memberId);
        return member.get().updateMember(updateMemberDto);
    }

    //회원 전쳬 조회
    public List<Member> findMembers(Long memberId) {
        return memberRepository.findByIdNot(memberId);
    }

    //회원 id로 조회
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    //회원 nickname으로 조회
    public List<Member> findByNickname(String nickname, Long memberId) {
        return memberRepository.findByNicknameAndIdNot(nickname, memberId);
    }

    //회원 email로 조회
    public Optional<Member> findByUserId(String userId, Long memberId) {
        return memberRepository.findByUserIdAndIdNot(userId, memberId);
    }

    public Member CreateMemberDtoToMember(CreateMemberDto memberDto) {
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

    public MemberInfoResponseDto createMemberInfoResponseDto(Member member) {
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .profilePath(member.getProfilePath())
                .name(member.getName())
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .mbti(member.getMbti())
                .build();
    }

    public SearchMemberResponseDto createSearchMemberResponseDto(Member member) {
        return SearchMemberResponseDto.builder()
                .id(member.getId())
                .profilePath(member.getProfilePath())
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .build();
    }
}
