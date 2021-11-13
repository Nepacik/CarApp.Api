package com.nepath.carapp.services.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.nepath.carapp.dtos.input.RefreshTokenDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.input.UserDeleteDto;
import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.models.RefreshToken;
import com.nepath.carapp.repositories.RefreshTokenRepository;
import com.nepath.carapp.security.enums.TokenType;
import com.nepath.carapp.exceptions.ApiRequestException;
import com.nepath.carapp.mappers.UserMapper;
import com.nepath.carapp.models.Role;
import com.nepath.carapp.models.User;
import com.nepath.carapp.repositories.CarRepository;
import com.nepath.carapp.repositories.UserRepository;
import com.nepath.carapp.security.extensions.CurrentUser;
import com.nepath.carapp.security.extensions.JWTExtensions;
import com.nepath.carapp.security.models.SecurityUserDetails;
import com.nepath.carapp.services.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTExtensions jwtExtensions;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNick(username);
        if (user == null) {
            log.error("user not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new SecurityUserDetails(user.getId(), user.getNick(), user.getPassword(), user.getRole().getName());
    }

    @Override
    public void saveToken(Long userId, String refreshToken) {
        User user = new User();
        user.setId(userId);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setName(refreshToken);

        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    @Transactional
    public TokenDto saveUser(UserCreateDto userCreateDto, String request) {
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
        String accessToken = jwtExtensions.createToken(user.getNick(), user.getId().toString(), user.getRole().getName(), request, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtExtensions.createToken(user.getNick(), user.getId().toString(), user.getRole().getName(), request, TokenType.REFRESH_TOKEN);
        saveToken(user.getId(), refreshToken);

        return new TokenDto(accessToken, refreshToken);

    }

    @SneakyThrows
    @Override
    public TokenDto refreshToken(RefreshTokenDto refreshTokenDto, String requestUrl) {
        RefreshToken refreshToken = refreshTokenRepository.findByName(refreshTokenDto.getRefreshToken());
        if (refreshToken == null) {
            log.error("User not found during refresh token attempt");

            throw new ApiRequestException.AuthorizationException();
        }
        User user = refreshToken.getUser();
        String accessToken = jwtExtensions.createToken(user.getNick(), user.getId().toString(), user.getRole().getName(), requestUrl, TokenType.ACCESS_TOKEN);

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

    @Override
    public void logout() {
        //Not the best solution, cause that will cause all sessions to expire, but the most important thing in this small app is to logout user no matter how
        refreshTokenRepository.deleteAllByUserId(CurrentUser.getUserId());
    }
}
