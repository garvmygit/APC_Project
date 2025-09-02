package org.GG.apcProject.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private Algorithm getAlg() {
        return Algorithm.HMAC256(secret);
    }

    // Generate token
    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(getAlg());
    }

    // Extract username
    public String getUsernameFromToken(String token) {
        DecodedJWT decoded = JWT.require(getAlg()).build().verify(token);
        return decoded.getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            JWT.require(getAlg()).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Invalid or expired token: " + e.getMessage());
            return false;
        }
    }
}
