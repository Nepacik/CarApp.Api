package com.nepath.carapp.services;

import com.nepath.carapp.dtos.output.TokenDto;
import com.nepath.carapp.security.SecurityUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {
    TokenDto createTokenDto(String username, String userId, String role, String requestUrl);
    TokenDto refreshToken(String refreshToken, String requestUrl) ;
}
