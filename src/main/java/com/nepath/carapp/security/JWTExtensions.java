package com.nepath.carapp.security;

import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;


@Component
public class JWTExtensions {
    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(JWTProperties.SECRET.getBytes());
    }
}