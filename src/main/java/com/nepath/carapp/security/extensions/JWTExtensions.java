package com.nepath.carapp.security.extensions;

import com.auth0.jwt.algorithms.Algorithm;
import com.nepath.carapp.security.properties.JWTProperties;
import org.springframework.stereotype.Component;


@Component
public class JWTExtensions {
    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(JWTProperties.SECRET.getBytes());
    }
}