package com.jia.consolerenter.security.jwt;

import com.jia.consolerenter.exception.UserAlreadyExistsException;
import com.jia.consolerenter.security.user.StoreUserDetails;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private long jwtExpirationMs;

    @Value("${auth.token.refreshExpirationInMils}")
    private long jwtRefreshExpirationMs;

    public String generateJwtTokenForUser(StoreUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        System.out.println("JWT Creation Time: " + now);
        System.out.println("JWT Expiry Time: " + expiryDate);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }





    public String generateRefreshToken(StoreUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }




    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
