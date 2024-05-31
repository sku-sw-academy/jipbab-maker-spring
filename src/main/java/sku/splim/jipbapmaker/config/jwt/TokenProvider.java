package sku.splim.jipbapmaker.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.User;

import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        logger.info("Generating token for user: {}", user.getEmail());
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        logger.info("Creating token with expiry date: {}", expiry);
        logger.info("Issuer: {}", jwtProperties.getIssuer());
        logger.info("User ID: {}", user.getId());
        logger.info("User Email: {}", user.getEmail());

        byte[] keyBytes = jwtProperties.getSecretKey().getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getId())

                .compact();
    }

    public boolean validToken(String token) {
        if(token == null || token.isEmpty()) return false;
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            logger.info("Token is valid: {}", token);
            return true;
        }
        catch (SecurityException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
        }
        catch (ExpiredJwtException e){
            logger.info("만료된 JWT 서명입니다.");
        }
        catch (UnsupportedJwtException e){
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        catch (Exception e) {
            logger.error("Invalid token: {}", token, e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        logger.info("Getting authentication for token: {}", token);

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities),
                token,
                authorities
        );
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        logger.info("Extracting user ID from token: {}", token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Failed to parse claims from token: {}", token, e);
            throw e;
        }
    }
}
