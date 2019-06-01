package com.tasteland.app.thetasteland.utils;

import com.tasteland.app.thetasteland.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class JwtUtils implements Serializable {

    private static final long serialVersionUID = 7326262609992241652L;
    private final Clock clock = DefaultClock.INSTANCE;
    private final Long expiration;
    private final String secret;


    public JwtUtils(
            @Value("${jwt.expiration}") Long expiration,
            @Value("${jwt.secret}") String secret) {
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

    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user);
    }

    private String createToken(Map<String, Object> claims, UserEntity user) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .setSubject(user.getEmail())
                .setIssuedAt(createdDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

}
