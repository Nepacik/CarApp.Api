package com.nepath.carapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nepath.carapp.exceptions.ApiException;
import com.nepath.carapp.exceptions.ApiRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();
        if (!requestPath.equals(JWTProperties.LOGIN_PATH) && !requestPath.equals(JWTProperties.REGISTRATION_PATH) && !requestPath.equals(JWTProperties.REFRESH_TOKEN_PATH)) {
            String authorizationHeader = request.getHeader(JWTProperties.HEADER_STRING);

            if (authorizationHeader != null && authorizationHeader.startsWith(JWTProperties.TOKEN_PREFIX)) {
                try {
                    String token = authorizationHeader.substring(JWTProperties.TOKEN_PREFIX.length());
                    DecodedJWT decodedJWT = JWT.require(JWTExtensions.getAlgorithm()).build().verify(token);

                    String username = decodedJWT.getClaim(JWTProperties.USERNAME).asString();
                    String role = decodedJWT.getClaim(JWTProperties.ROLE).asString();
                    Long userId = Long.parseLong(decodedJWT.getClaim(JWTProperties.USER_ID).asString());

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));
                    IdUsernamePasswordAuthenticationToken authenticationToken = new IdUsernamePasswordAuthenticationToken(username, null, authorities, userId);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                } catch (Exception exception) {
                    log.error(exception.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    ApiException apiException = new ApiException("Authorization failed", HttpStatus.UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), apiException);
                    return;
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
