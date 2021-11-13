package com.nepath.carapp.security.extensions;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.nepath.carapp.security.enums.TokenType;
import com.nepath.carapp.security.properties.JWTProperties;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JWTExtensions {
    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(JWTProperties.SECRET.getBytes());
    }

    public static String createToken(String username, String userId, String role, String requestUrl, TokenType tokenType) {

        int tokenExpirationTime;
        switch(tokenType) {
            case ACCESS_TOKEN:
                tokenExpirationTime = JWTProperties.ACCESS_TOKEN_EXPIRATION_TIME;
                break;
            case REFRESH_TOKEN:
                tokenExpirationTime = JWTProperties.REFRESH_TOKEN_EXPIRATION_TIME;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tokenType);
        }

        return JWT.create()
                .withClaim(JWTProperties.USERNAME, username)
                .withClaim(JWTProperties.USER_ID, userId)
                .withClaim(JWTProperties.ROLE, role)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .withIssuer(requestUrl)
                .sign(JWTExtensions.getAlgorithm());
    }

    public static String getUsernameFromToken(String token) {
        token = token.replace(JWTProperties.TOKEN_PREFIX, "");
        JWTVerifier verifier = JWT.require(JWTExtensions.getAlgorithm()).build();
        return verifier.verify(token).getClaim(JWTProperties.USERNAME).asString();
    }
}