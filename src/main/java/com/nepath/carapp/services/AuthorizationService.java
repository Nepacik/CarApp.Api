package com.nepath.carapp.services;

import com.nepath.carapp.dtos.input.LoginDto;
import com.nepath.carapp.dtos.input.RefreshTokenDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.input.UserDeleteDto;
import com.nepath.carapp.dtos.output.TokenDto;

public interface AuthorizationService extends SecurityUserDetailsService {
    TokenDto refreshToken(RefreshTokenDto refreshTokenDto, String requestUrl);

    TokenDto saveUser(UserCreateDto userCreateDto, String request);

    void deleteUser(UserDeleteDto userDeleteDto);

    void logout();
}
