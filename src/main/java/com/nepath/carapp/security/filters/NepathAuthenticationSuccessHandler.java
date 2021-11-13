package com.nepath.carapp.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.repositories.RefreshTokenRepository;
import com.nepath.carapp.security.enums.TokenType;
import com.nepath.carapp.security.models.SecurityUserDetails;
import com.nepath.carapp.security.extensions.JWTExtensions;
import com.nepath.carapp.services.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NepathAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final SecurityUserDetailsService userDetailsService;
    private final JWTExtensions jwtExtensions;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityUserDetails principal = (SecurityUserDetails) authentication.getPrincipal();
        String accessToken = jwtExtensions.createToken(principal.getUsername(), principal.getUserId().toString(), principal.getRole(), request.getRequestURL().toString(), TokenType.ACCESS_TOKEN);
        String refreshToken = jwtExtensions.createToken(principal.getUsername(), principal.getUserId().toString(), principal.getRole(), request.getRequestURL().toString(), TokenType.REFRESH_TOKEN);

        try {
            userDetailsService.saveToken(principal.getUserId(), refreshToken);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokenDto);
    }
}
