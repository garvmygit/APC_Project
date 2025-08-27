package org.GG.apcProject.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private Algorithm getAlg() { return Algorithm.HMAC256(secret); }

    public String generateToken(String username) {
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
            .sign(getAlg());
    }

    public String validateAndGetUsername(String token) {
        return JWT.require(getAlg()).build().verify(token).getSubject();
    }
}
