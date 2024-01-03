package hello.movie.repository;

import hello.movie.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 생성")
    void createMember(){
        //given
        Member member1 = Member.builder().userId("testA").password("testA").build();
        Member member2 = Member.builder().userId("testB").password("testB").build();

        //when
        Member result1 = memberRepository.save(member1);
        Member result2 = memberRepository.save(member2);

        //then
        assertThat(result1.getUserId()).isEqualTo(member1.getUserId());
        assertThat(result2.getUserId()).isEqualTo(member2.getUserId());
    }
}