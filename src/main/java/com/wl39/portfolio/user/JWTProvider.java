package com.wl39.portfolio.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JWTProvider {
    @Value("${JWT_SECRET}")
    private String secretKey;
    private final long expirationMs = 3600000; // 1 hours

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secretKey);
    }

    public String generateToken(String email, String username, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("username", username)
                .withClaim("roles", List.of("ROLE_" + role))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(getAlgorithm());
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(getAlgorithm()).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getEmailFromJWT(String token) {
        DecodedJWT decodedJWT = JWT.require(getAlgorithm())
                .build()
                .verify(token);
        return decodedJWT.getSubject();
    }


    public String getUsernameFromJWT(String token) {
        DecodedJWT decoded = JWT.require(getAlgorithm()).build().verify(token);
        return decoded.getClaim("username").asString();
    }


    public List<String> getRolesFromJWT(String token) {
        DecodedJWT decoded = JWT.require(getAlgorithm()).build().verify(token);
        return decoded.getClaim("roles").asList(String.class);
    }
}
