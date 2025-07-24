package com.wl39.portfolio.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTProvider {
    @Value("${JWT_SECRET}")
    private String secretKey;
    private final long expirationMs = 3600000; // 1 hours
    private final long refreshExpirationMs = 7 * 24 * 60 * 60 * 1000L; // 7 days

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secretKey);
    }

    public String generateToken(String email, String username, String rolesStr) {
        // 1. "ADMIN,TEACHER" → ["ROLE_ADMIN", "ROLE_TEACHER"]
        List<String> roles = Arrays.stream(rolesStr.split(","))
                .map(String::trim)
                .map(role -> "ROLE_" + role)
                .collect(Collectors.toList());

        return JWT.create()
                .withSubject(email)
                .withClaim("username", username)
                .withClaim("roles", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(getAlgorithm());
    }

    public String issueRegisterToken(String email, String provider, String username) {
        return JWT.create()
                .withSubject("oauth-register")
                .withClaim("email", email)
                .withClaim("provider", provider)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5분
                .sign(getAlgorithm());
    }



    public String generateRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationMs))
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

    public DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(getAlgorithm()).build().verify(token);
        } catch (JWTVerificationException e) {
            return null;
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
