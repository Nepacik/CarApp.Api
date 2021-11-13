package com.nepath.carapp.services;

import com.nepath.carapp.dtos.input.LoginDto;
import com.nepath.carapp.dtos.input.RefreshTokenDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.input.UserDeleteDto;
import com.nepath.carapp.dtos.output.TokenDto;

public interface AuthorizationService {
    TokenDto registerCreateToken(UserCreateDto userCreateDto, String requestUrl);

    TokenDto refreshToken(RefreshTokenDto refreshTokenDto, String requestUrl);

    void saveUser(UserCreateDto userCreateDto);

    void deleteUser(UserDeleteDto userDeleteDto);
}
