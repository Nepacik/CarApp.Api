package com.nepath.carapp.services;

import com.nepath.carapp.dtos.input.RefreshTokenDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.output.TokenDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {
    TokenDto createTokenDto(String username, String userId, String role, String requestUrl);

    TokenDto registerCreateToken(UserCreateDto userCreateDto, String requestUrl);

    TokenDto refreshToken(RefreshTokenDto refreshTokenDto, String requestUrl);
}
