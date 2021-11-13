package com.nepath.carapp.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nepath.carapp.exceptions.ApiException;
import com.nepath.carapp.security.extensions.JWTExtensions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NepathApiKeyFilter extends OncePerRequestFilter {

    private final JWTExtensions jwtExtensions;

    public NepathApiKeyFilter(JWTExtensions jwtExtensions) {
        this.jwtExtensions = jwtExtensions;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader("ApiKey");
        if(apiKey == null || !apiKey.equals(jwtExtensions.apiKey)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiException apiException = new ApiException("Authorization Error", HttpStatus.UNAUTHORIZED);
            new ObjectMapper().writeValue(response.getOutputStream(), apiException);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
