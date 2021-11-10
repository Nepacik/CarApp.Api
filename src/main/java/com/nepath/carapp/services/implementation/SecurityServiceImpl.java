package com.nepath.carapp.services.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.enums.TokenType;
import com.nepath.carapp.exceptions.ApiRequestException;
import com.nepath.carapp.models.User;
import com.nepath.carapp.repositories.UserRepository;
import com.nepath.carapp.security.JWTExtensions;
import com.nepath.carapp.security.JWTProperties;
import com.nepath.carapp.security.SecurityUserDetails;
import com.nepath.carapp.services.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    @Override
    public TokenDto createTokenDto(String username, String userId, String role, String requestUrl) {

        String accessToken = createToken(username, userId, role, requestUrl, TokenType.ACCESS_TOKEN);
        String refreshToken = createToken(username, userId, role, requestUrl, TokenType.REFRESH_TOKEN);

        return new TokenDto(accessToken, refreshToken);
    }

    @SneakyThrows
    @Override
    public TokenDto refreshToken(String refreshToken, String requestUrl) {
        JWTVerifier verifier = JWT.require(JWTExtensions.getAlgorithm()).build();
        String username = verifier.verify(refreshToken).getClaim(JWTProperties.USERNAME).asString();

        User user = userRepository.findByNick(username);
        if (user == null) {
            log.error("User not found during refresh token attempt");

            throw new ApiRequestException.NotFoundErrorException("asfafasf");
        }

        String accessToken = createToken(user.getNick(), user.getId().toString(), user.getRole().getName(), requestUrl, TokenType.ACCESS_TOKEN);

        return new TokenDto(accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNick(username);
        if (user == null) {
            log.error("user not found");
            throw new ApiRequestException.AuthorizationException();
        }
        return new SecurityUserDetails(user.getId(), user.getNick(), user.getPassword(), user.getRole().getName());
    }

    private String createToken(String username, String userId, String role, String requestUrl, TokenType tokenType) {

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
}
