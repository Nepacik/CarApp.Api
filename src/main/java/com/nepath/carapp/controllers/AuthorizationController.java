package com.nepath.carapp.controllers;

import com.nepath.carapp.dtos.input.LoginDto;
import com.nepath.carapp.dtos.input.RefreshTokenDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.services.SecurityService;
import com.nepath.carapp.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/authorization")
@RestController
@Validated
public class AuthorizationController {

    private final UserService userService;
    private final SecurityService securityService;

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest request, @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        TokenDto tokenDto = securityService.refreshToken(refreshTokenDto, request.getRequestURL().toString());
        return ResponseEntity.ok().body(tokenDto);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDto> createUser(HttpServletRequest request, @Valid @RequestBody UserCreateDto userCreateDto) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/authorization/users").toUriString());
        userService.saveUser(userCreateDto);
        TokenDto tokenDto = securityService.registerCreateToken(userCreateDto, request.getRequestURL().toString());
        return ResponseEntity.created(uri).body(tokenDto);
    }

    @ApiOperation("Login.")
    @PostMapping("/login")
    public void fakeLogin(@RequestBody LoginDto loginDto) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}
