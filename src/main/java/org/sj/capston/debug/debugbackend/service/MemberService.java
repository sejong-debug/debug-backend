package org.sj.capston.debug.debugbackend.service;

import lombok.RequiredArgsConstructor;
import org.sj.capston.debug.debugbackend.dto.JoinDto;
import org.sj.capston.debug.debugbackend.repository.MemberRepository;
import org.sj.capston.debug.debugbackend.entity.Member;
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
    public long join(JoinDto joinDto) {
        Member member = Member.builder()
                .username(joinDto.getUsername())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .build();
        Member save = memberRepository.save(member);
        return save.getId();
    }
}
