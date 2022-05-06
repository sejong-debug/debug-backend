package org.sj.capstone.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capstone.debug.debugbackend.dto.member.MemberDto;
import org.sj.capstone.debug.debugbackend.dto.member.MemberJoinDto;
import org.sj.capstone.debug.debugbackend.error.ErrorCode;
import org.sj.capstone.debug.debugbackend.error.exception.BusinessException;
import org.sj.capstone.debug.debugbackend.repository.MemberRepository;
import org.sj.capstone.debug.debugbackend.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    @Transactional
    public long join(MemberJoinDto memberJoinDto) {
        if (memberRepository.existsByUsername(memberJoinDto.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_DUPLICATION,
                    ">> inputted username=" + memberJoinDto.getUsername());
        }
        Member member = Member.builder()
                .username(memberJoinDto.getUsername())
                .password(passwordEncoder.encode(memberJoinDto.getPassword()))
                .name(memberJoinDto.getName())
                .build();
        return memberRepository.save(member).getId();
    }

    public boolean checkDuplicateUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    public MemberDto getByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                        ">> username=" + username));
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(member.getUsername());
        memberDto.setName(member.getName());
        return memberDto;
    }
}
