package org.sj.capstone.debug.debugbackend.service;

import org.junit.jupiter.api.Test;
import org.sj.capstone.debug.debugbackend.dto.member.MemberJoinDto;
import org.sj.capstone.debug.debugbackend.repository.MemberRepository;
import org.sj.capstone.debug.debugbackend.entity.Member;
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

        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setUsername(username);
        memberJoinDto.setPassword(password);
        memberJoinDto.setName(name);

        long joinedMemberId = memberService.join(memberJoinDto);
        Member joinedMember = memberRepository.findById(joinedMemberId).orElseThrow(
                () -> new RuntimeException("회원가입 실패")
        );

        assertThat(joinedMember.getId()).isEqualTo(joinedMemberId);
        assertThat(joinedMember.getUsername()).isEqualTo(username);
        assertThat(joinedMember.getName()).isEqualTo(name);
    }
}