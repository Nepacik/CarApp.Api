package com.nepath.carapp.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nepath.carapp.exceptions.ApiException;
import com.nepath.carapp.security.models.IdUsernamePasswordAuthenticationToken;
import com.nepath.carapp.security.models.SecurityUserDetails;
import com.nepath.carapp.security.extensions.JWTExtensions;
import com.nepath.carapp.security.properties.JWTProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class NepathAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final JWTExtensions jwtExtensions;
    private final boolean isDev;

    public NepathAuthorizationFilter(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JWTExtensions jwtExtensions, boolean isDev) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtExtensions = jwtExtensions;
        this.isDev = isDev;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUrl = request.getRequestURL().toString();
        if(!requestUrl.equals(JWTProperties.LOGIN_PATH) && !requestUrl.equals(JWTProperties.REGISTRATION_PATH) && !requestUrl.equals(JWTProperties.REFRESH_TOKEN_PATH)) {
            IdUsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            if (authentication == null) {
                filterChain.doFilter(request, response);
                if(!isDev) {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    ApiException apiException = new ApiException("Authorization Error", HttpStatus.UNAUTHORIZED);
                    new ObjectMapper().writeValue(response.getOutputStream(), apiException);
                }
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private IdUsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JWTProperties.HEADER_STRING);
        if (token != null && token.startsWith(JWTProperties.TOKEN_PREFIX)) {
            String username;
            try {
                username = jwtExtensions.getUsernameFromAccessToken(token);
            } catch (JWTVerificationException e) {
                return null;
            }
            if (username != null) {
                SecurityUserDetails userDetails = (SecurityUserDetails) userDetailsService.loadUserByUsername(username);

                return new IdUsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities(), userDetails.getUserId());
            }
        }
        return null;
    }
}
