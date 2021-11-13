package com.nepath.carapp.security.extensions;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.nepath.carapp.security.enums.TokenType;
import com.nepath.carapp.security.properties.JWTProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTExtensions {

    @Value("${nepath.secretAccessToken}")
    private String secretAccessToken;

    @Value("${nepath.secretRefreshToken}")
    private String secretRefreshToken;

    private Algorithm getAccessTokenAlgorithm() {
        return Algorithm.HMAC256(secretAccessToken.getBytes());
    }


    private Algorithm getRefreshTokenAlgorithm() {
        return Algorithm.HMAC256(secretRefreshToken.getBytes());
    }


    public String createToken(String username, String userId, String role, String requestUrl, TokenType tokenType) {

        int tokenExpirationTime;
        Algorithm algorithm;
        switch (tokenType) {
            case ACCESS_TOKEN:
                tokenExpirationTime = JWTProperties.ACCESS_TOKEN_EXPIRATION_TIME;
                algorithm = getAccessTokenAlgorithm();
                break;
            case REFRESH_TOKEN:
                tokenExpirationTime = JWTProperties.REFRESH_TOKEN_EXPIRATION_TIME;
                algorithm = getRefreshTokenAlgorithm();
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
                .sign(algorithm);
    }

    public String getUsernameFromAccessToken(String token) {
        token = token.replace(JWTProperties.TOKEN_PREFIX, "");
        JWTVerifier verifier = JWT.require(this.getAccessTokenAlgorithm()).build();
        return verifier.verify(token).getClaim(JWTProperties.USERNAME).asString();
    }
}