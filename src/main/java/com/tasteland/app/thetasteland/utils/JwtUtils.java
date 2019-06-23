package com.tasteland.app.thetasteland.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Data
public class JwtUtils implements Serializable {

    private static final long serialVersionUID = 7326262609992241652L;
    private final Clock clock = DefaultClock.INSTANCE;
    private final Long expiration;
    private final String secret;
    private final String tokenHeader;
    private final String jwtPrefix;

    public JwtUtils(
            @Value("${jwt.expiration}") Long expiration,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.header}") String tokenHeader,
            @Value("${jwt.prefix}") String jwtPrefix
    ) {
        this.tokenHeader = tokenHeader;
        this.jwtPrefix = jwtPrefix;
        this.expiration = expiration;
        this.secret = secret;
    }


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, claim -> claim.getSubject());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, claim -> claim.getExpiration());
    }

    public Date getIssuedDateFromToken(String token) {
        return getClaimFromToken(token, claim -> claim.getIssuedAt());
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Boolean isExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(clock.now());
    }

    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities());
        claims.put("enabled", user.isEnabled());
        claims.put("expired", user.isAccountNonExpired());
        claims.put("locked", user.isAccountNonLocked());
        return createToken(claims, user);
    }

    private String createToken(Map<String, Object> claims, UserDetails user) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .setSubject(user.getUsername())
                .setIssuedAt(createdDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

    public boolean containsToken(String tokenHeader) {
        return tokenHeader != null && tokenHeader.startsWith(getJwtPrefix());
    }

    public Boolean isValidToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isExpired(token)
                        && expiration.after(clock.now()));
    }
}
