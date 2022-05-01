package org.sj.capstone.debug.debugbackend.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MemberContext extends User {

    private final long memberId;

    public MemberContext(
            long memberId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.memberId = memberId;
    }
}
