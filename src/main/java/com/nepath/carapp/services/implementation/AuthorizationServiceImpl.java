package com.nepath.carapp.services.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.nepath.carapp.dtos.input.RefreshTokenDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.input.UserDeleteDto;
import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.security.enums.TokenType;
import com.nepath.carapp.exceptions.ApiRequestException;
import com.nepath.carapp.mappers.UserMapper;
import com.nepath.carapp.models.Role;
import com.nepath.carapp.models.User;
import com.nepath.carapp.repositories.CarRepository;
import com.nepath.carapp.repositories.UserRepository;
import com.nepath.carapp.security.extensions.CurrentUser;
import com.nepath.carapp.security.extensions.JWTExtensions;
import com.nepath.carapp.security.properties.JWTProperties;
import com.nepath.carapp.services.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CarRepository carRepository;

    @Override
    public TokenDto registerCreateToken(UserCreateDto userCreateDto, String requestUrl) {
        User user = userRepository.findByNick(userCreateDto.getNick());
        if (user == null) {
            throw new ApiRequestException.AuthorizationException();
        }
        return createTokenDto(user.getNick(), user.getId().toString(), user.getRole().getName(), requestUrl);
    }

    @Override
    public void saveUser(UserCreateDto userCreateDto) {
        User user = userMapper.createUserToUser(userCreateDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setId(2L);
        user.setRole(role);
        if(userRepository.existsUserByEmail(user.getEmail())) {
            throw new ApiRequestException.ConflictException("Email already exists");
        }
        if(userRepository.existsUserByNick(user.getNick())) {
            throw new ApiRequestException.ConflictException("Nick already exists");
        }
        userRepository.save(user);
    }

    @SneakyThrows
    @Override
    public TokenDto refreshToken(RefreshTokenDto refreshTokenDto, String requestUrl) {
        String username = JWTExtensions.getUsernameFromToken(refreshTokenDto.getRefreshToken());
        User user = userRepository.findByNick(username);
        if (user == null) {
            log.error("User not found during refresh token attempt");

            throw new ApiRequestException.AuthorizationException();
        }
        String accessToken = JWTExtensions.createToken(user.getNick(), user.getId().toString(), user.getRole().getName(), requestUrl, TokenType.ACCESS_TOKEN);

        return new TokenDto(accessToken, refreshTokenDto.getRefreshToken());
    }

    @Transactional
    @Async
    @Override
    public void deleteUser(UserDeleteDto userDeleteDto) {
        User user = userRepository.findByNick(CurrentUser.getUserName());

        if(passwordEncoder.matches(userDeleteDto.getPassword(), user.getPassword())) {
            carRepository.removeCarOwner(CurrentUser.getUserId());
            userRepository.deleteById(CurrentUser.getUserId());
        } else {
            throw new ApiRequestException.AuthorizationException();
        }
    }

    private TokenDto createTokenDto(String username, String userId, String role, String requestUrl) {

        String accessToken = JWTExtensions.createToken(username, userId, role, requestUrl, TokenType.ACCESS_TOKEN);
        String refreshToken = JWTExtensions.createToken(username, userId, role, requestUrl, TokenType.REFRESH_TOKEN);

        return new TokenDto(accessToken, refreshToken);
    }
}
