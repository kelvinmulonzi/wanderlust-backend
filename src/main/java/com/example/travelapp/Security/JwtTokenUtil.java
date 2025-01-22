package com.example.travelapp.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    // Change to non-static to avoid potential initialization issues
    private final SecretKey secretKey;
    private static final String ISSUER = "travelapp";

    public JwtTokenUtil() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        logger.info("JwtTokenUtil initialized with new secret key");
    }

    public String generateJWT(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                logger.error("Username is null or empty");
                throw new IllegalArgumentException("Username cannot be null or empty");
            }

            logger.debug("Generating JWT token for username: {}", username);

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + 3600 * 1000); // 1 hour

            String token = Jwts.builder()
                    .setIssuer(ISSUER)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(secretKey, SignatureAlgorithm.HS256) // Explicitly specify algorithm
                    .compact();

            logger.debug("JWT token generated successfully");
            return token;

        } catch (Exception e) {
            logger.error("Failed to generate JWT token", e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public Claims extractClaims(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                logger.error("Token is null or empty");
                throw new IllegalArgumentException("Token cannot be null or empty");
            }

            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Failed to extract claims from token", e);
            throw new RuntimeException("Failed to extract claims", e);
        }
    }

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            logger.error("Error checking token expiration", e);
            return true;
        }
    }

    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return (username.equals(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            logger.error("Error validating token", e);
            return false;
        }
    }
}