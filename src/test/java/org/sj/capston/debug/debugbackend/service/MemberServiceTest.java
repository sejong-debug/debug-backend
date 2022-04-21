package org.sj.capston.debug.debugbackend.service;

import org.junit.jupiter.api.Test;
import org.sj.capston.debug.debugbackend.dto.JoinDto;
import org.sj.capston.debug.debugbackend.entity.Member;
import org.sj.capston.debug.debugbackend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void joinTest() {
        String username = "test-username";
        String password = "test-password!";
        String name = "test-name";

        JoinDto joinDto = new JoinDto();
        joinDto.setUsername(username);
        joinDto.setPassword(password);
        joinDto.setName(name);

        long joinedMemberId = memberService.join(joinDto);
        Member joinedMember = memberRepository.findById(joinedMemberId).orElseThrow(
                () -> new RuntimeException("회원가입 실패")
        );

        assertThat(joinedMember.getId()).isEqualTo(joinedMemberId);
        assertThat(joinedMember.getUsername()).isEqualTo(username);
        assertThat(joinedMember.getName()).isEqualTo(name);
    }
}