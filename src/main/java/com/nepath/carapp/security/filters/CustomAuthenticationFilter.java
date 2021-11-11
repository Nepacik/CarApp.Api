package com.nepath.carapp.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nepath.carapp.dtos.input.LoginDto;
import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.security.properties.JWTProperties;
import com.nepath.carapp.security.SecurityUserDetails;
import com.nepath.carapp.services.SecurityService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public
class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final SecurityService securityService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, SecurityService securityService) {
        this.authenticationManager = authenticationManager;
        this.securityService = securityService;

        setFilterProcessesUrl(JWTProperties.LOGIN_PATH);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken;
        try {
            LoginDto loginDto = new ObjectMapper().readValue(request.getReader(), LoginDto.class);
            authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword());
        } catch (IOException e) {
            throw new BadCredentialsException("Invalid request");
        }
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        SecurityUserDetails user = (SecurityUserDetails) authentication.getPrincipal();

        TokenDto tokenDto = securityService.createTokenDto(user.getUsername(), user.getUserId().toString(), user.getRole(), request.getRequestURL().toString());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokenDto);
    }
}
