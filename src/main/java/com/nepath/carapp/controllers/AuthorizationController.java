package com.nepath.carapp.controllers;

import com.nepath.carapp.dtos.input.LoginDto;
import com.nepath.carapp.dtos.input.RefreshTokenDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.input.UserDeleteDto;
import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.services.AuthorizationService;
import com.nepath.carapp.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/authorization")
@RestController
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest request, @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        TokenDto tokenDto = authorizationService.refreshToken(refreshTokenDto, request.getRequestURL().toString());
        return ResponseEntity.ok().body(tokenDto);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDto> createUser(HttpServletRequest request, @Valid @RequestBody UserCreateDto userCreateDto) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/authorization/users").toUriString());
        TokenDto tokenDto = authorizationService.saveUser(userCreateDto, request.getRequestURL().toString());
        return ResponseEntity.created(uri).body(tokenDto);
    }

    @Secured("IS_AUTHENTICATED_FULLY")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authorizationService.logout();
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Login.")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> fakeLogin(@RequestBody LoginDto loginDto) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @Secured("IS_AUTHENTICATED_FULLY")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody UserDeleteDto userDeleteDto) {
        authorizationService.deleteUser(userDeleteDto);
        return ResponseEntity.noContent().build();
    }
}
