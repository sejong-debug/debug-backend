package org.sj.capston.debug.debugbackend.security;

import lombok.RequiredArgsConstructor;
import org.sj.capston.debug.debugbackend.entity.Member;
import org.sj.capston.debug.debugbackend.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 username: " + username));
        return new PrincipalDetails(member.getUsername(), member.getPassword(), member.getId());
    }
}
