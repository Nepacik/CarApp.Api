package com.nepath.carapp.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nepath.carapp.dtos.input.CarCreateDto;
import com.nepath.carapp.dtos.input.RefreshTokenDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.input.UserDeleteDto;
import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.enums.TokenType;
import com.nepath.carapp.mappers.UserMapper;
import com.nepath.carapp.models.User;
import com.nepath.carapp.security.JWTExtensions;
import com.nepath.carapp.services.SecurityService;
import com.nepath.carapp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/authorization")
@RestController
public class AuthorizationController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final SecurityService securityService;

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest request, @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        TokenDto tokenDto = securityService.refreshToken(refreshToken, request.getRequestURL().toString());
        return ResponseEntity.ok().body(tokenDto);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDto> createUser(HttpServletRequest request, @Valid @RequestBody UserCreateDto userCreateDto) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/authorization/users").toUriString());
        User user = userMapper.createUserToUser(userCreateDto);
        userService.saveUser(user);
        TokenDto tokenDto = securityService.createTokenDto(user.getNick(), user.getId().toString(), user.getRole().getName(), request.getRequestURL().toString());
        return ResponseEntity.created(uri).body(tokenDto);
    }
}
