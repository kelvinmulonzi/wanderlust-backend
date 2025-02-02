package com.example.travelapp.security;

import com.auth0.json.auth.UserInfo;
import com.example.travelapp.models.User;
import com.example.travelapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Component
public class JWTUtil {
    @Autowired
    UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        Optional<User> user = userRepository.findByUsername(username);
       //Object roles= user.get().getAuthorities().stream().map(role->role.getAuthority());

        return Jwts.builder()

                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public String generateResetPasswordJwtToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email) // Set the subject (who is the token about)
                .setIssuedAt(now) // Set the issued date
                .setExpiration(new Date(now.getTime() + 1000*60*10)) // Set expiration time (10 minutes)
                .signWith(SignatureAlgorithm.HS256, secret) // Sign the JWT using the secret key
                .compact(); // Compact the JWT into a string
    }
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public String getSecret(){
        return secret;
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
