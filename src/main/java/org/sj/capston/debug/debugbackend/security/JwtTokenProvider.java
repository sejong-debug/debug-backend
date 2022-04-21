package org.sj.capston.debug.debugbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String AUTHORITIES_KEY = "auth";

    private static final String MEMBER_ID_KEY = "member_id";

    private final long tokenValidityInMilliseconds;

    private final SecretKey secretKey;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long memberId = ((MemberContext) authentication.getPrincipal()).getMemberId();

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(MEMBER_ID_KEY, memberId)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String authoritiesToken = claims.get(AUTHORITIES_KEY).toString();

        Collection<? extends GrantedAuthority> authorities = StringUtils.hasText(authoritiesToken) ?
                Arrays.stream(authoritiesToken.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()) : null;

        long memberId = Long.parseLong(claims.get(MEMBER_ID_KEY).toString());

        MemberContext memberContext = new MemberContext(
                memberId, claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(memberContext, null, memberContext.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: ", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: ", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: ", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: ", e);
        }
        return false;
    }
}
